/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thiel
 */
class WebWorker implements HttpConstants, Runnable {

    private final static int BUF_SIZE = 2048;
    private static final byte[] EOL = {(byte) '\r', (byte) '\n'};

    /* static map */
    private final static Hashtable<String, String> staticMap = createMimeMap();
    /**
     * private copy of mimeMap
     */
    private final Hashtable<String, String> mimeMap = new Hashtable(staticMap);

    /* buffer to use for requests */
    private byte[] buf = new byte[BUF_SIZE];

    /* Socket to client we're handling */
    private Socket socket = null;
    /**
     * indicates whether the thread is ordered to stop
     */
    private boolean stopping = false;

    public synchronized void setSocket(Socket s) {
        this.socket = s;
        notify();
    }

    public void stop() {
        this.stopping = true;
    }

    @Override
    public synchronized void run() {

        while (!stopping) {
            if (socket == null) {
                /* nothing to do */
                try {
                    wait();
                } catch (InterruptedException e) {
                    /* should not happen */
                    continue;
                }
            }

            synchronized (this) { //while handling the call the socket needs to be locked
                try {
                    handleClient();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                            null, ex);
                }

                socket = null;
            }
            //try to add this thread to the pool
            WebServer.getInstance().addThreadToPool(this);
        }
    }

    private int readBuffer(InputStream is) throws IOException {
        /* zero out the buffer from last time */
        for (int i = 0; i < BUF_SIZE; i++) {
            buf[i] = 0;
        }

        /* We only support HTTP GET/HEAD, and don't
         * support any fancy HTTP options,
         * so we're only interested really in
         * the first line.
         */
        int readBytes = 0, r = 0;

        outerloop:
        while (readBytes < BUF_SIZE) {
            r = is.read(buf, readBytes, BUF_SIZE - readBytes);
            if (r == -1) {
                /* EOF */
                return -1;
            }
            int i = readBytes;
            readBytes += r;
            for (; i < readBytes; i++) {
                if (buf[i] == (byte) '\n' || buf[i] == (byte) '\r') {
                    /* read one line */
                    break outerloop;
                }
            }
        }
        return readBytes;

    }

    private WebRequest getTargetFile(InputStream is) throws UnsupportedHTTPMethodException, IOException {

        int readBytes = readBuffer(is);
        if (readBytes == -1) {
            return null;
        }

        String query = new String(buf, Charset.forName("US-ASCII"));

        if (query.startsWith("HEAD ")) {
            //FIXME we don't handle this call at the moment
            return null;
        } else if (!query.startsWith("GET ")) {
            throw new UnsupportedHTTPMethodException();
        }

        /* find the file name, from:
         * GET /foo/bar.html HTTP/1.0
         * extract "/foo/bar.html"
         * GET --> start at position 4
         */
        WebRequest result = new WebRequest();
        for (int i = "GET ".length(); i < readBytes; i++) {
            if ((buf[i] == (byte) ' ')
                    || (buf[i] == (byte) '?')) {

                //set fname
                result.fname = (query.substring("GET ".length(), i))
                        .replace('/', File.separatorChar);
                if (result.fname.startsWith(File.separator)) {
                    result.fname = result.fname.substring(1);
                }

                //set param
                if (buf[i] == (byte) '?'){
                    for (int j = i+1; j < readBytes; j++) {
                        if (buf[j] == (byte) ' '){
                            result.param = query.substring(i+1,j);
                            break;
                        }
                    }
                }

                break;

            }
        }


        return result;
    }

    private void handleClient() throws IOException {

        InputStream is = new BufferedInputStream(socket.getInputStream());
        PrintStream ps = new PrintStream(socket.getOutputStream());
        /* we will only block in read for this many milliseconds
         * before we fail with java.io.InterruptedIOException,
         * at which point we will abandon the connection.
         */

        socket.setSoTimeout(WebServer.getInstance().getTimeOut());
        socket.setTcpNoDelay(true);
        try {
            WebRequest request = null;
            try {
                 request = getTargetFile(is);
            } catch (UnsupportedHTTPMethodException ex) {
                /* we don't support this method */
                ps.print("HTTP/1.0 " + HTTP_BAD_METHOD
                        + " unsupported method type: ");
                ps.write(buf, 0, 5);
                ps.write(EOL);
                ps.flush();
                socket.close();
                return;
            }
            if (request == null) {
                socket.close();
                return;
            }

            Logger.getLogger(WebWorker.class.getName()).log(Level.FINE,
                    "request:{0}", request);

            //if we find a fitting service call the service
            ServiceEndpoint service = ServiceEndpoints.getInstance().getEndpoint(request.fname);
            if (service != null) {
                Logger.getLogger(WebWorker.class.getName()).log(Level.FINE, 
                        "found service");

                printDynamicPage(service.getContentType(), service.handleCall(request), ps);
            } else {
                //look for a fitting file/directory
                File targetFile = new File(WebServer.getInstance().getRoot(), request.fname);
                if (targetFile.isDirectory()) {
                    File indexFile = new File(targetFile, "index.html");
                    if (indexFile.exists()) {
                        targetFile = indexFile;
                    }
                }
                if (printHeaders(targetFile, ps)) {
                    sendFile(targetFile, ps);
                } else {
                    send404(ps);
                }
            }
            ps.flush();
            Logger.getLogger(WebWorker.class.getName()).log(Level.FINE, "done.");

        } finally {
            socket.close();
        }
    }

    private void printDynamicPage(String contentType,
            String content, PrintStream ps) throws IOException {

        Logger.getLogger(WebWorker.class.getName()).log(Level.FINE,
                "content:\n{0}", content);


        ps.print("HTTP/1.0 " + HTTP_OK + " OK");
        ps.write(EOL);
        ps.print("Server: SIT java");
        ps.write(EOL);
        ps.print("Date: " + (new Date()));
        ps.write(EOL);

        ps.print("Content-length: " + content.length());
        ps.write(EOL);
        ps.print("Last Modified: " + Calendar.getInstance().getTime());
        ps.write(EOL);
        ps.print("Content-type: " + contentType);
        ps.write(EOL);
        ps.print("Connection: close");
        ps.write(EOL);
        ps.write(EOL);
        ps.print(content);


    }

    private boolean printHeaders(File targetFile, PrintStream ps) throws IOException {
        boolean result = false;
        int returnCode = 0;

        if (!targetFile.exists()) {
            returnCode = HTTP_NOT_FOUND;
            ps.print("HTTP/1.0 " + HTTP_NOT_FOUND + " not found");
            ps.write(EOL);
            result = false;
        } else {
            returnCode = HTTP_OK;
            ps.print("HTTP/1.0 " + HTTP_OK + " OK");
            ps.write(EOL);
            result = true;
        }
        Logger.getLogger(WebWorker.class.getName()).log(Level.INFO,
                "From {0}: GET {1}-->{2}",
                new Object[]{socket.getInetAddress().getHostAddress(),
                    targetFile.getAbsolutePath(), returnCode});

        ps.print("Server: SIT java");
        ps.write(EOL);
        ps.print("Date: " + (new Date()));
        ps.write(EOL);

        if (result) {
            if (!targetFile.isDirectory()) {
                ps.print("Content-length: " + targetFile.length());
                ps.write(EOL);
                ps.print("Last Modified: " + (new Date(targetFile.lastModified())));
                ps.write(EOL);
                String name = targetFile.getName();
                int ind = name.lastIndexOf('.');
                String contentType = null;
                if (ind > 0) {
                    contentType = mimeMap.get(name.substring(ind));
                }
                if (contentType == null) {
                    contentType = "unknown/unknown";
                }
                ps.print("Content-type: " + contentType);
                ps.write(EOL);
            } else {
                ps.print("Content-type: text/html");
                ps.write(EOL);
            }
        }
        return result;
    }

    private void send404(PrintStream ps) throws IOException {
        ps.write(EOL);
        ps.write(EOL);
        ps.println("Not Found\n\n"
                + "The requested resource was not found.\n");
    }

    private void sendFile(File targetFile, PrintStream ps) throws IOException {
        InputStream is = null;
        ps.write(EOL);

        //handle directory
        if (targetFile.isDirectory()) {
            listDirectory(targetFile, ps);
            return;

            //handle file
        } else {
            is = new FileInputStream(targetFile.getAbsolutePath());
        }
        try {
            int n;
            while ((n = is.read(buf)) > 0) {
                ps.write(buf, 0, n);
            }
        } finally {
            is.close();
        }
    }

    private void listDirectory(File dir, PrintStream ps) throws IOException {
        ps.println("<TITLE>Directory listing</TITLE><P>\n");
        ps.println("<A HREF=\"..\">Parent Directory</A><BR>\n");
        String[] list = dir.list();
        for (int i = 0; list != null && i < list.length; i++) {
            File f = new File(dir, list[i]);
            if (f.isDirectory()) {
                ps.println("<A HREF=\"" + list[i] + "/\">" + list[i] + "/</A><BR>");
            } else {
                ps.println("<A HREF=\"" + list[i] + "\">" + list[i] + "</A><BR");
            }
        }
        ps.println("<P><HR><BR><I>" + (new Date()) + "</I>");
    }

    private static java.util.Hashtable<String, String> createMimeMap() {
        Hashtable<String, String> result = new Hashtable();
        result.put("", "content/unknown");
        result.put(".uu", "application/octet-stream");
        result.put(".exe", "application/octet-stream");
        result.put(".ps", "application/postscript");
        result.put(".zip", "application/zip");
        result.put(".sh", "application/x-shar");
        result.put(".tar", "application/x-tar");
        result.put(".snd", "audio/basic");
        result.put(".au", "audio/basic");
        result.put(".wav", "audio/x-wav");
        result.put(".gif", "image/gif");
        result.put(".jpg", "image/jpeg");
        result.put(".jpeg", "image/jpeg");
        result.put(".htm", "text/html");
        result.put(".html", "text/html");
        result.put(".text", "text/plain");
        result.put(".c", "text/plain");
        result.put(".cc", "text/plain");
        result.put(".c++", "text/plain");
        result.put(".h", "text/plain");
        result.put(".pl", "text/plain");
        result.put(".txt", "text/plain");
        result.put(".java", "text/plain");
        return result;
    }
}
