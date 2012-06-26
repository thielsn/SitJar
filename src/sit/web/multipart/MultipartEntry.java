/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.multipart;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import sit.web.HttpConstants;

/**
 *
 * @author simon
 */
public abstract class MultipartEntry {

    
    protected TYPES type;
    protected String filename = null;
    protected String name = null;
    protected String contentType;

    public MultipartEntry(TYPES type, String mimetype, String name) {
        this.type = type;
        this.name = name;

        if ((type == TYPES.BINARY) || (type == TYPES.UNKNOWN)) {
            this.contentType = HttpConstants.MIME_APPLICATION_OCTETSTREAM;
        } else if (type == TYPES.TEXT) {
            this.contentType = "text/plain";
        } else if (type == TYPES.MIME) {
            this.contentType = mimetype;
        } else {
            throw new RuntimeException("unsupported type:" + type);
        }
    }
    
    public abstract long getContentLengthOfContent();
    
    public long getContentLength(){
        
        return getContentLengthOfContent() + getHeader().length();
    }

    public String getHeader() {
        StringBuilder result = new StringBuilder(HttpConstants.CONTENT_DISPOSITION_TAG);

        if (name != null) {
            result.append(HttpConstants.NAME_DISPOSITION_TAG).append("\"").append(name).append("\"; ");
        }
        if (filename != null) {
            result.append(HttpConstants.FILENAME_DISPOSITION_TAG).append("\"").append(filename).append("\"; ");
        }
        result.append(HttpConstants.CRLF).append(HttpConstants.CONTENT_TYPE_TAG).append(contentType).append(HttpConstants.CRLF);

        if ((type == TYPES.BINARY) || (contentType.equals(HttpConstants.MIME_APPLICATION_OCTETSTREAM))) {
            result.append(HttpConstants.CONTENT_TRANSFER_ENCODING_BINARY_TAG).append(HttpConstants.CRLF);
        }
        result.append(HttpConstants.CRLF);


        return result.toString();
    }
    
    

    //the length of each item 
    public void writeTo(OutputStream out, String boundary) throws IOException {
        DataOutputStream output = new DataOutputStream(out);
               
        // write out the data
        output.writeBytes(boundary+HttpConstants.CRLF);
        output.writeBytes(getHeader());

        writePartContentTo(out);
        output.writeBytes(HttpConstants.CRLF);
    }

    protected abstract void writePartContentTo(OutputStream out) throws IOException;

    public TYPES getType() {
        return type;
    }

    public String getFileName() {
        return filename;
    }
}
