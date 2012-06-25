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

    protected static final String MIME_APPLICATION_OCTETSTREAM = "application/octet-stream";
    /*
     * Content-Disposition: form-data; name="file"; filename="picknick.jpg"
     * Content-Type: application/octet-stream Content-Transfer-Encoding
     *
     *
     *
     *
     *
     * Need to create a POST request that uploads a file? I spent this afternoon
     * trying to figure out what one looks like. This document includes a
     * template that has been successful for me.
     *
     *
     * ======================== POST /path/to/script.php HTTP/1.0 Host:
     * example.com Content-type: multipart/form-data, boundary=AaB03x
     * Content-Length: $requestlen
     *
     * --AaB03x content-disposition: form-data; name="field1"
     *
     * $field1 --AaB03x content-disposition: form-data; name="field2"
     *
     * $field2 --AaB03x content-disposition: form-data; name="userfile";
     * filename="$filename" Content-Type: $mimetype Content-Transfer-Encoding:
     * binary
     *
     * $binarydata --AaB03x-- ==========================
     *     
*
     *
     * http://stackoverflow.com/questions/101662/what-http-header-to-use-for-setting-form-field-names-multipart-form-data
     * http://publib.boulder.ibm.com/infocenter/ltscnnct/v2r0/index.jsp?topic=/com.ibm.connections.25.help/r_api_files_create_file_multipart.html
     */
    public final String CONTENT_DISPOSITION_TAG = "Content-Disposition: form-data; ";
    public final String CONTENT_TYPE_TAG = "Content-Type: ";
    public final String CONTENT_TRANSFER_ENCODING_TAG = "Content-Transfer-Encoding: binary";
    protected TYPES type;
    protected String filename = null;
    protected String name = null;
    protected String contentType;

    public MultipartEntry(TYPES type, String mimetype, String name) {
        this.type = type;
        this.name = name;

        if ((type == TYPES.BINARY) || (type == TYPES.UNKNOWN)) {
            this.contentType = MIME_APPLICATION_OCTETSTREAM;
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
        StringBuilder result = new StringBuilder(CONTENT_DISPOSITION_TAG);

        if (name != null) {
            result.append("name=\"").append(name).append("\"; ");
        }
        if (filename != null) {
            result.append("filename=\"").append(filename).append("\"; ");
        }
        result.append(HttpConstants.CRLF).append(CONTENT_TYPE_TAG).append(contentType).append(HttpConstants.CRLF);

        if ((type == TYPES.BINARY) || (contentType.equals(MIME_APPLICATION_OCTETSTREAM))) {
            result.append(CONTENT_TRANSFER_ENCODING_TAG).append(HttpConstants.CRLF);
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
}
