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



    /* static map */
    private final static Hashtable<String, String> staticMap = createMimeMap();
    /**
     * private copy of mimeMap
     */
    private final Hashtable<String, String> mimeMap = new Hashtable(staticMap);

    /* buffer to use for requests */
    private WebBuffer buf = new WebBuffer();
    
    private HTTPHelper httphelp = new HTTPHelper();

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
        Logger.getLogger(WebWorker.class.getName()).log(Level.INFO, "stop WebWorker");
    }

    @Override
    public void run() {

        while (!stopping) {
            synchronized (this) {
                if (socket == null) {
                    /* nothing to do */
                    try {
                        wait(); //wait releases the monitor
                    } catch (InterruptedException e) {
                        continue;
                    }
                }
                try {
                    handleClient();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }

                socket = null;
            }
            //try to add this thread to the pool
            WebServer.getInstance().addThreadToPool(this);
        }
    }

    private WebRequest getWebRequest(InputStream is) throws UnsupportedHTTPMethodException, IOException, MessageTooLargeException {


        return httphelp.getHeaderAndBody(is, buf).getWebRequest();
        
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
                request = getWebRequest(is);
            } catch (UnsupportedHTTPMethodException ex) {
                /* we don't support this method */
                ps.print("HTTP/1.0 " + HTTP_BAD_METHOD
                        + " unsupported method type: ");
                buf.writeToPrintStream(ps, 0, 5);
                ps.write(WebBuffer.EOL);
                ps.flush();
                socket.close();
                return;
            } catch (MessageTooLargeException ex) {

                String message = "HTTP/1.0 " + HTTP_ENTITY_TOO_LARGE
                        + " Entity Too Large";
                Logger.getLogger(WebWorker.class.getName()).log(Level.WARNING, message);
                ps.print(message);
                ps.write(WebBuffer.EOL);
                ps.flush();
                socket.close();
                return;
            }
            if (request == null) {
                socket.close();
                return;
            }

            Logger.getLogger(WebWorker.class.getName()).log(Level.FINE,
                    "fname:{0}", request);

            //if we find a fitting service call the service
            ServiceEndpoint service = ServiceEndpoints.getInstance().getEndpoint(request.fname);
            if (service != null) {
                Logger.getLogger(WebWorker.class.getName()).log(Level.INFO,
                        "found service:"+service.getEndpointName());

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

    private void printDynamicPage(String contentType, String content, PrintStream ps) throws IOException {

        Logger.getLogger(WebWorker.class.getName()).log(Level.FINE, "content:\n{0}", content);

        ps.print("HTTP/1.0 " + HTTP_OK + " OK");
        ps.write(WebBuffer.EOL);
        ps.print("Server: SIT java");
        ps.write(WebBuffer.EOL);
        ps.print("Date: " + (new Date()));
        ps.write(WebBuffer.EOL);

        ps.print("Content-length: " + content.length());
        ps.write(WebBuffer.EOL);
        ps.print("Last Modified: " + Calendar.getInstance().getTime());
        ps.write(WebBuffer.EOL);
        ps.print("Content-type: " + contentType);
        ps.write(WebBuffer.EOL);
        ps.print("Connection: close");
        ps.write(WebBuffer.EOL);
        ps.write(WebBuffer.EOL);
        ps.print(content);


    }

    private boolean printHeaders(File targetFile, PrintStream ps) throws IOException {
        boolean result = false;
        int returnCode = 0;

        if (!targetFile.exists()) {
            returnCode = HTTP_NOT_FOUND;
            ps.print("HTTP/1.0 " + HTTP_NOT_FOUND + " not found");
            ps.write(WebBuffer.EOL);
            result = false;
        } else {
            returnCode = HTTP_OK;
            ps.print("HTTP/1.0 " + HTTP_OK + " OK");
            ps.write(WebBuffer.EOL);
            result = true;
        }
        Logger.getLogger(WebWorker.class.getName()).log(Level.INFO,
                "From {0}: GET {1}-->{2}",
                new Object[]{socket.getInetAddress().getHostAddress(),
                    targetFile.getAbsolutePath(), returnCode});

        ps.print("Server: SIT java");
        ps.write(WebBuffer.EOL);
        ps.print("Date: " + (new Date()));
        ps.write(WebBuffer.EOL);

        if (result) {
            if (!targetFile.isDirectory()) {
                ps.print("Content-length: " + targetFile.length());
                ps.write(WebBuffer.EOL);
                ps.print("Last Modified: " + (new Date(targetFile.lastModified())));
                ps.write(WebBuffer.EOL);
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
                ps.write(WebBuffer.EOL);
            } else {
                ps.print("Content-type: text/html");
                ps.write(WebBuffer.EOL);
            }
        }
        return result;
    }

    private void send404(PrintStream ps) throws IOException {
        ps.write(WebBuffer.EOL);
        ps.write(WebBuffer.EOL);
        ps.println("<h1>Not Found</h1><br/><br/>\n\n"
                + "The requested resource was not found.\n");
    }

    private void send403(PrintStream ps) throws IOException {
        ps.write(WebBuffer.EOL);
        ps.write(WebBuffer.EOL);
        ps.println("<h1>Forbidden</h1><br/><br/>\n\n"
                + "Access to the requested resource was denied.\n");
    }

    private void sendFile(File targetFile, PrintStream ps) throws IOException {
        InputStream is = null;
        ps.write(WebBuffer.EOL);

        //handle directory
        if (targetFile.isDirectory()) {
            if (WebServer.getInstance().isPermitDirectoryListing()) {
                listDirectory(targetFile, ps);
            } else {
                send403(ps);
            }
            return;

            //handle file
        } else {
            is = new FileInputStream(targetFile.getAbsolutePath());
        }
        try {
            int n;
            while ((n = buf.readFromInputStream(is)) > 0) {
                buf.writeToPrintStream(ps, 0, n);
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
