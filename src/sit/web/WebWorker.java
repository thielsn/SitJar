/*
* Copyright 2013 Simon Thiel
*
* This file is part of SitJar.
*
* SitJar is free software: you can redistribute it and/or modify
* it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SitJar is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.
*/

/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 */
package sit.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.ByteBuilder;
import sit.web.client.HttpHelper;
import sit.web.socket.SocketI;

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
    private SocketI socket = null;
    /**
     * indicates whether the thread is ordered to stop
     */
    private boolean stopping = false;
    
    private final static Logger logger = Logger.getLogger(WebWorker.class.getName());    
    private final static Level level = logger.getLevel();
    
    private final static String e404Msg="<html><body><h1>Not Found</h1><br/><br/>\n\n"
                + "The requested resource was not found.\n</body></html>";
    private final static String e403Msg="<html><body><h1>Forbidden</h1><br/><br/>\n\n"
                + "Access to the requested resource was denied.\n</body></html>";
    
    private final static String e500Msg="<html><body><h1>Internal Server Error</h1><br/><br/>\n\n"
                + "The server had an internal error.\n</body></html>";
            
    public synchronized void setSocket(SocketI s) {
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
                               
                String message = "unsupported method type: " + new String(buf.getBytes(0, 5));
                sendHtmlMessageAndClose(HTTP_BAD_METHOD, message, ps);
                return;
                
            } catch (MessageTooLargeException ex) {
                sendHtmlMessageAndClose(HTTP_ENTITY_TOO_LARGE, "Entity Too Large", ps);
                logger.log(Level.WARNING, "Entity Too Large:\n" + ex.getMessage());  
                return;
                
            } catch (HTTPParseException ex) {
                sendHtmlMessageAndClose(HTTP_SERVER_ERROR, "Internal Server Error", ps);                      
                logger.log(Level.WARNING, "Internal Server Error:\n" + ex.getMessage());
               
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
                logger.log(Level.FINE, "found service:" + service.getEndpointName());

                printDynamicPage(service.getContentTypeAsString(), service.getCharSet(), service.handleCall(request), ps);
            } else {
               handleFileAndDirectoryCall(request, ps);
            }

        } finally {
            socket.close();
        }
    }
    
    
    private void handleFileAndDirectoryCall(WebRequest request, PrintStream ps) throws IOException{
         
        String filename=HttpHelper.decodeString(request.fname);
        //look for a fitting file/directory
        File targetFile = new File(WebServer.getInstance().getRoot(), filename);

        //check 404
        if (!targetFile.exists()) {
            sendHtmlMessageAndClose(HTTP_NOT_FOUND, e404Msg, ps, request.contentType.charSet);
            return;
        }
        //check indexfile       
        if (targetFile.isDirectory()) {
            File indexFile = new File(targetFile, "index.html");
            if (indexFile.exists()) {
                targetFile = indexFile;
            }
        }
        //check directory listing
        if (targetFile.isDirectory()) { //could have been replaced by index.html
            //handle directory
            if (WebServer.getInstance().isPermitDirectoryListing()) {
                String body = getDirectoryList(targetFile);
                sendHtmlMessageAndClose(HTTP_OK, body, ps, request.contentType.charSet);
                return;
            } else {                
                sendHtmlMessageAndClose(HTTP_FORBIDDEN, e403Msg, ps, request.contentType.charSet);
                return;
            }
        }else{
            
            ByteBuilder headers = getHeaders(HTTP_OK,MimeTypes.getMimeTypeFromFileName(targetFile.getName()), 
                    request.contentType.charSet, targetFile.length());
            
            sendFileAndClose(headers.toByteArray(), targetFile, ps);
            return;
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
        
        closeCall(output);

    }
    
    
    private ByteBuilder getHeaders(int returnCode, String mimeType, Charset charset, long contentLength){
        ByteBuilder result = new ByteBuilder();
        
        String returnCodeMessage = HttpConstantsHelper.getHTTPCodeMessage(returnCode);
        if (returnCodeMessage==null){
            Logger.getLogger(WebWorker.class.getName()).log(Level.WARNING, "no message found for HTTP code:"+returnCode);
            returnCodeMessage="";
        }
        
        
        result.append(("HTTP/1.0 " + returnCode + " "+returnCodeMessage).getBytes(charset));
        result.append(HttpConstants.CRLF_BYTE);
        
        result.append("Server: SIT java 0.2".getBytes(charset));
        result.append(HttpConstants.CRLF_BYTE);
        
        result.append(("Date: " + (new Date())).getBytes(charset));
        result.append(HttpConstants.CRLF_BYTE);
        
        result.append((CONTENT_TYPE_TAG + mimeType
                +HttpConstants.SUB_FIELD_SEPARATOR+HttpConstants.CHARSET_CONTENT_TYPE_TAG+charset.name()).getBytes(charset));
        result.append(CRLF_BYTE);
        
        result.append((CONTENT_LENGTH_TAG + contentLength).getBytes(charset));
        result.append(CRLF_BYTE);
        
        result.append(CRLF_BYTE);
        return result;
        
    }
    
        
    private void closeCall(OutputStream os) throws IOException{
        os.flush();
        socket.close();
    }
    
    private void sendHtmlMessageAndClose(String htmlMessage, PrintStream ps) throws IOException{
        sendHtmlMessageAndClose(HTTP_OK, htmlMessage, ps,  HttpConstants.DEFAULT_CHARSET);
    }
    
    private void sendHtmlMessageAndClose(int returnCode, String htmlMessage, PrintStream ps) throws IOException{
        sendHtmlMessageAndClose(returnCode, htmlMessage, ps,  HttpConstants.DEFAULT_CHARSET);
    }
    
    private void sendHtmlMessageAndClose(int returnCode, String htmlMessage, PrintStream ps, Charset charset) throws IOException{
    
        byte[] message = htmlMessage.getBytes(charset);
        
        ByteBuilder headers = getHeaders(returnCode, DEFAULT_MIME_TYPE, charset, message.length);
        sendBytesAndClose(headers.toByteArray(), message, ps); 
    }
    
    
    private void sendBytesAndClose(byte[] header, byte[] body, PrintStream ps) throws IOException{
        ps.write(header);
        ps.write(body);
        closeCall(ps);
    } 


    private void sendFileAndClose(byte[] header, File targetFile, PrintStream ps) throws IOException {
        
        ps.write(header);
        
        InputStream is = null;
     
        is = new FileInputStream(targetFile.getAbsolutePath());
    
        try {
            int n;
            while ((n = buf.readFromInputStream(is)) > 0) {
                buf.writeToOutStream(ps, 0, n);
            }
        } finally {
            is.close();
        }
        
        closeCall(ps);
    }
    
    private String getMyPath(File dir){
        
        String myPath = ServiceEndpointHelper.replaceBackSlashes(
                (dir.getPath().length() > 0) ? dir.getPath() : "");
        
        try{//remove previously added prefix from the path
            myPath = myPath.substring(WebServer.getInstance().getRoot().getPath().length());
        }catch (Exception ex){
            Logger.getLogger(WebWorker.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        if (!myPath.startsWith("/")){
            myPath="/"+myPath;
        }
        if (myPath.length()==0 || (!myPath.endsWith("/"))){
            myPath+="/";
        }
                
        return myPath;
    }

    private String getDirectoryList(File dir)  {
        String myPath = getMyPath(dir);

        StringBuilder result = new StringBuilder();
        result.append("<html><head><title>Directory listing</title></head>\n");

        result.append("<body><h1>Directory listing</h1>\n");
        result.append("<p><a href=\"..\">[Parent directory]</a><br/>\n");
        
        
        
        File[] list = dir.listFiles();
        for (int i = 0; list != null && i < list.length; i++) {
            File f = list[i];
            if (f.isDirectory()) {
                result.append("<a href=\"" + myPath + f.getName() + "/\">" + f.getName() + "/</a><br/>\n");
            } else {
                result.append("<a href=\"" + myPath + f.getName() + "\">" + f.getName() + "</a><br/>\n");
            }
        }
        result.append("<br/></p><p><hr></p><p><i>" + (new Date()) + "</i></p></body></html>");
        
        return result.toString();
    }
}
