/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.web.multipart.MultipartParser;
import sit.web.multipart.TYPES;

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

        public void parseContentType(String contentTypePayload) {

            String[] contentTypeFields = contentTypePayload.split(";");
            
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
                        this.charSet = Charset.forName(myValue);
                    } catch (IllegalCharsetNameException ex) {
                        Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "charset :" + myValue + " illegal! Using " + charSet.name() + " instead!");
                    } catch (UnsupportedCharsetException ex) {
                        Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "charset :" + myValue + " not supported! Using " + charSet.name() + " instead!");
                    }
                }
                 
            }
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
                result += headerEntry.getKey() + ": " + headerEntry.getValue() + "\n";
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
                result += headerEntry.getKey() + ": " + headerEntry.getValue() + "\n";
            }
            result += "\n";
        }
        if (body != null) {
            result += "\nBody:\n" + getBodyAsString() + "\n----\n";
        }
        return result;
    }

    public String getBodyAsString() {
        return new String(body, contentType.charSet);
    }
}
