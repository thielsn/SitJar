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

import java.io.File;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class HTTPMessage {
                                               //was 2097152 previously - why?
    public final static int MAX_MESSAGE_SIZE = Integer.MAX_VALUE-1;
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
            
            int firstSpace = lines[i].indexOf(' ');
            String name = lines[i].substring(0, firstSpace);
            String value = "";
            if (lines[i].length()>firstSpace+1){
                value = lines[i].substring(firstSpace+1);
            }

            if (i==0){ // the first line contains the httpcommand e.g. GET /wiki/Http HTTP/1.1
                result.httpCommand = name.toUpperCase();
            }
            result.headerItems.put(name.toUpperCase(), value);
            
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
        uri = uri.substring(0, uri.indexOf(' '));

        int paramStart = uri.indexOf('?');

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
        String contentTypeEntry = webRequest.headerItems.get(HttpConstants.CONTENT_TYPE_TAG.toUpperCase().trim());  //TODO handle case issue !!!//TODO introduce real handling of Request properties trim is here only required to remove the trailing space :-(
        if (contentTypeEntry!=null){
           webRequest.contentType.parseContentType(contentTypeEntry);
        }
        
        return webRequest;
    }

    boolean hasHeader() {
        return header!=null;
    }
    
  
}
