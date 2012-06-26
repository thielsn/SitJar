/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.web.multipart.MultipartParser;
import sit.web.multipart.TYPES;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class HTTPMessage {

    public final static int MAX_MESSAGE_SIZE = 2097152;
    private WebRequest webRequest = null;
    private String header = null;

   
    public void setHeader(String header) {
        this.header = header;
    }

    private WebRequest parseHTTPHeader(String header){
        
   
        String [] lines = header.split(HttpConstants.CRLF);
        WebRequest result = new WebRequest();
        result.headerItems = new Hashtable();
        for (int i=0;i<lines.length;i++){
            
            int firstSpace = lines[i].indexOf(" ");
            String name = lines[i].substring(0, firstSpace);
            String value = "";
            if (lines[i].length()>firstSpace+1){
                value = lines[i].substring(firstSpace+1);
            }

            if (i==0){ // the first line contains the httpcommand e.g. GET /wiki/Http HTTP/1.1
                result.httpCommand = name;
            }
            result.headerItems.put(name, value);
            
        }

        return result;
    }

    public WebRequest getWebRequest() {

        if (webRequest != null) {
            return webRequest;
        }
        if (header == null) {
            Logger.getLogger(HTTPMessage.class.getName()).log(Level.SEVERE, "header is null !!");
            return null;
        }
      
        webRequest = parseHTTPHeader(header);

        String uri = webRequest.headerItems.get(webRequest.httpCommand);
        uri = uri.substring(0, uri.indexOf(" "));

        int paramStart = uri.indexOf("?");

        if (paramStart > 0 && paramStart < uri.length()) {
            webRequest.fname = uri.substring(0, paramStart);
            webRequest.param = uri.substring(paramStart + 1);
        } else {
            webRequest.fname = uri;
        }

        //adjust filename
        webRequest.fname = webRequest.fname.replace('/', File.separatorChar);
        if (webRequest.fname.startsWith(File.separator)) {
            webRequest.fname = webRequest.fname.substring(1);
        }

        //parse contentType
        String contentTypeEntry = webRequest.headerItems.get(HttpConstants.CONTENT_TYPE_TAG.trim());  //TODO introduce real handling of Request properties trim is here only required to remove the trailing space :-(
        if (contentTypeEntry!=null){
           webRequest.contentType.parseContentType(contentTypeEntry);
        }
        
        return webRequest;
    }

    boolean hasHeader() {
        return header!=null;
    }
    
  
}
