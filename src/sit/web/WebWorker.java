/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thiel
 */
class WebWorker implements HttpConstants, Runnable {

    /*
     * buffer to use for requests
     */
    private WebBuffer buf = new WebBuffer();
    private HTTPParser httphelp = new HTTPParser();

    /*
     * Socket to client we're handling
     */
    private Socket socket = null;
    /**
     * indicates whether the thread is ordered to stop
     */
    private boolean stopping = false;
    
    private final static Logger logger = Logger.getLogger(WebWorker.class.getName());    
    private final static Level level = logger.getLevel();

    public synchronized void setSocket(Socket s) {
        this.socket = s;
        notify();
    }

    public void stop() {
        this.stopping = true;
        logger.log(Level.FINE, "stop WebWorker");
    }

    @Override
    public void run() {

        while (!stopping) {
            synchronized (this) {
                if (socket == null) {
                    /*
                     * nothing to do
                     */
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

    private WebRequest getWebRequest(InputStream is, PrintStream ps) throws UnsupportedHTTPMethodException, IOException, MessageTooLargeException, HTTPParseException {

        HTTPMessage result = httphelp.getHeaderAndBody(is, buf, ps);

        if (result == null) {
            return null;
        }
        return result.getWebRequest();
    }

    private void handleClient() throws IOException {

        InputStream is = new BufferedInputStream(socket.getInputStream());
        PrintStream ps = new PrintStream(socket.getOutputStream());
        /*
         * we will only block in read for this many milliseconds before we fail
         * with java.io.InterruptedIOException, at which point we will abandon
         * the connection.
         */

        socket.setSoTimeout(WebServer.getInstance().getTimeOut());
        socket.setTcpNoDelay(true);
        try {
            WebRequest request = null;
            try {
                request = getWebRequest(is, ps);
            } catch (UnsupportedHTTPMethodException ex) {
                /*
                 * we don't support this method
                 */
                ps.print("HTTP/1.0 " + HTTP_BAD_METHOD
                        + " unsupported method type: ");
                buf.writeToOutStream(ps, 0, 5);
                ps.write(HttpConstants.CRLF_BYTE);
                ps.flush();
                socket.close();
                return;
            } catch (MessageTooLargeException ex) {

                String message = "HTTP/1.0 " + HTTP_ENTITY_TOO_LARGE
                        + " Entity Too Large";
                logger.log(Level.WARNING, message);
                ps.print(message);
                ps.write(HttpConstants.CRLF_BYTE);
                ps.flush();
                socket.close();
                return;
            } catch (HTTPParseException ex) {
                String message = "HTTP/1.0 " + HTTP_SERVER_ERROR
                        + " Internal Server Error";
                logger.log(Level.WARNING, message + "\n" + ex.getMessage());
                ps.print(message + "\n" + ex.getMessage());
                ps.write(HttpConstants.CRLF_BYTE);
                ps.flush();
                socket.close();
                return;
            }
            if (request == null) {
                socket.close();
                return;
            }

            if (logger.getLevel() == Level.FINE) {
                logger.log(Level.FINE, "request:{0}", request.toBriefString());
            } else{
                logger.log(Level.FINER, "request:{0}", request.toString());
            }


            //if we find a fitting service call the service
            ServiceEndpoint service = ServiceEndpoints.getInstance().getEndpoint(request.fname);
            if (service != null) {
                logger.log(Level.FINE,
                        "found service:" + service.getEndpointName());

                printDynamicPage(service.getContentTypeAsString(), service.getCharSet(), service.handleCall(request), ps);
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
            //logger.log(Level.INFO, "done.");

        } finally {
            socket.close();
        }
    }

    private void printDynamicPage(String contentType, Charset charSet, byte[] content, FilterOutputStream output)
            throws IOException {

        if (charSet == null) {
            charSet = DEFAULT_CHARSET;
        }

        logger.log(Level.FINE, "content:\n{0}", new String(content, charSet));

        output.write(("HTTP/1.0 " + HTTP_OK + " OK").getBytes(charSet));
        output.write(CRLF_BYTE);
        output.write(("Server: SIT java").getBytes(charSet));
        output.write(CRLF_BYTE);
        output.write(("Date: " + (new Date())).getBytes(charSet));
        output.write(CRLF_BYTE);

        output.write((CONTENT_LENGTH_TAG + content.length).getBytes(charSet));
        output.write(CRLF_BYTE);
        output.write(("Last Modified: " + Calendar.getInstance().getTime()).getBytes(charSet));
        output.write(CRLF_BYTE);
        output.write((CONTENT_TYPE_TAG + contentType).getBytes(charSet));
        output.write(CRLF_BYTE);
        output.write(("Connection: close").getBytes(charSet));
        output.write(CRLF_BYTE);
        output.write(CRLF_BYTE);
        output.write(content);


    }

    private boolean printHeaders(File targetFile, PrintStream ps) throws IOException {
        boolean result = false;
        int returnCode = 0;

        if (!targetFile.exists()) {
            returnCode = HTTP_NOT_FOUND;
            ps.print("HTTP/1.0 " + HTTP_NOT_FOUND + " not found");
            ps.write(HttpConstants.CRLF_BYTE);
            result = false;
        } else {
            returnCode = HTTP_OK;
            ps.print("HTTP/1.0 " + HTTP_OK + " OK");
            ps.write(HttpConstants.CRLF_BYTE);
            result = true;
        }
        logger.log(Level.FINE,
                "From {0}: GET {1}-->{2}",
                new Object[]{socket.getInetAddress().getHostAddress(),
                    targetFile.getAbsolutePath(), returnCode});

        ps.print("Server: SIT java");
        ps.write(HttpConstants.CRLF_BYTE);
        ps.print("Date: " + (new Date()));
        ps.write(HttpConstants.CRLF_BYTE);

        if (result) {
            if (!targetFile.isDirectory()) {
                ps.print("Content-length: " + targetFile.length());
                ps.write(CRLF_BYTE);
                ps.print("Last Modified: " + (new Date(targetFile.lastModified())));
                ps.write(CRLF_BYTE);

                String contentType = MimeTypes.getMimeTypeFromFileName(targetFile.getName());

                ps.print(CONTENT_TYPE_TAG + contentType);
                ps.write(CRLF_BYTE);
            } else {
                ps.print(CONTENT_TYPE_TAG + DEFAULT_MIME_TYPE);
                ps.write(CRLF_BYTE);
            }
        }
        return result;
    }

    private void send404(PrintStream ps) throws IOException {
        ps.write(CRLF_BYTE);
        ps.write(CRLF_BYTE);
        ps.println("<h1>Not Found</h1><br/><br/>\n\n"
                + "The requested resource was not found.\n");
    }

    private void send403(PrintStream ps) throws IOException {
        ps.write(CRLF_BYTE);
        ps.write(CRLF_BYTE);
        ps.println("<h1>Forbidden</h1><br/><br/>\n\n"
                + "Access to the requested resource was denied.\n");
    }

    private void sendFile(File targetFile, PrintStream ps) throws IOException {
        InputStream is = null;
        ps.write(CRLF_BYTE);

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
                buf.writeToOutStream(ps, 0, n);
            }
        } finally {
            is.close();
        }
    }

    private void listDirectory(File dir, PrintStream ps) throws IOException {


        ps.println("<html><head><title>Directory listing</title></head>\n");

        ps.println("<body><h1>Directory listing</h1>\n");
        ps.println("<p><a href=\"..\">[Parent directory]</a><br/>\n");
        
        String myPath = ServiceEndpointHelper.replaceBackSlashes(
                (dir.getPath().length() > 0) ? dir.getPath() : "");
        try{//remove previously added prefix from the path
            myPath = myPath.substring(WebServer.getInstance().getRoot().getPath().length());
        }catch (Exception ex){
            Logger.getLogger(WebWorker.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        if (myPath.startsWith("/")){
            myPath = myPath.substring(1);
        }
        
        File[] list = dir.listFiles();
        for (int i = 0; list != null && i < list.length; i++) {
            File f = list[i];
            if (f.isDirectory()) {
                ps.println("<a href=\"/" + myPath + "/" + f.getName() + "/\">" + f.getName() + "/</a><br/>\n");
            } else {
                ps.println("<a href=\"/" + myPath + "/" + f.getName() + "\">" + f.getName() + "</a><br/>\n");
            }
        }
        ps.println("<br/></p><p><hr></p><p><i>" + (new Date()) + "</i></p></body></html>");
    }
}