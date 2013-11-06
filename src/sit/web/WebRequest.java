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

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.web.multipart.MultipartParser;

/**
 *
 * @author thiel
 */
public class WebRequest {

    public String httpCommand = null;
    public String fname = null;
    public String param = null;
    public byte[] body = null;
    public ContentType contentType = new ContentType();
    public Hashtable<String, String> headerItems = null;

    public static class ContentType {
        

        public String mimeType = HttpConstants.DEFAULT_MIME_TYPE; //init with default mimetype
        public Charset charSet = HttpConstants.DEFAULT_CHARSET;
        public String boundary = null; //only for multipart content types

        public void parseContentType(String contentTypePayload) {

            String[] contentTypeFields = contentTypePayload.split(HttpConstants.SUB_FIELD_SEPARATOR);
            
            if (contentTypeFields.length==0){
                return;
            }
            //handle mimetype
            mimeType = contentTypeFields[0].trim();
            
            //since in content-type the second field could only be charset - no further general parsing done here
            if (contentTypeFields.length==2){
                String myValue = HTTPParseHelper.getValueIfExists(HttpConstants.CHARSET_CONTENT_TYPE_TAG, contentTypeFields[1]); 
                if (myValue != null) {
                    try {
                        this.charSet = Charset.forName(myValue.trim());
                    } catch (IllegalCharsetNameException ex) {
                        Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "charset :" + myValue + " illegal! Using " + charSet.name() + " instead!");
                    } catch (UnsupportedCharsetException ex) {
                        Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "charset :" + myValue + " not supported! Using " + charSet.name() + " instead!");
                    }
                }
                myValue = HTTPParseHelper.getValueIfExists(HttpConstants.BOUNDARY_CONTENT_TYPE_PREFIX, contentTypeFields[1]); 
                if (myValue != null) {
                    boundary = myValue.trim();
                }
                 
            }
        }

        public boolean isMultiPart() {
            return boundary!=null;
        }
    }

    public String toBriefString() {
        String result = httpCommand + " " + fname;
        if (param != null) {
            result += "?" + param;
        }
        if (headerItems != null) {
            result += "\nHeader:\n";
            for (Entry<String, String> headerEntry : headerItems.entrySet()) {
                result += headerEntry.getKey() + " " + headerEntry.getValue() + "\n";
            }
            result += "\n";
        }
        if (body != null) {
            result += "\nBody: (" + body.length + " bytes)";
        }
        return result;
    }

    @Override
    public String toString() {
        String result = httpCommand + " " + fname;
        if (param != null) {
            result += "?" + param;
        }
        if (headerItems != null) {
            result += "\nHeader:\n";
            for (Entry<String, String> headerEntry : headerItems.entrySet()) {
                result += headerEntry.getKey() + " " + headerEntry.getValue() + "\n";
            }
            result += "\n";
        }
        if (body != null) {
            result += "\n" + getBodyAsString() + "\n\n";
        }
        return result;
    }

    public String getBodyAsString() {
        
        //##CHARSET_MARKER##        
        if (contentType==null || contentType.charSet==null){
            return new String(body);
        }
        return new String(body, contentType.charSet);
    }
    
    /**
     * returns a specific entry from the header 
     * double point at the end of a term is required - if
     * case is ignored
     * 
     * @param key
     * @return  
     */
    public String getHeaderEntry(String key){
       return headerItems.get(key.toUpperCase());
    }
}
