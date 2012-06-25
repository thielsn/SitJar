/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.multipart;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.web.HttpConstants;

/**
 *
 * @author simon
 */
public class MultipartContainer {
    public static final String FINAL_BORDER = "--"+HttpConstants.CRLF;
    
    /*
    POST /path/to/script.php HTTP/1.0
    Host: example.com
    Content-type: multipart/form-data, boundary=AaB03x
    Content-Length: $requestlen

--AaB03x */
    
    private String boundary = "----------------"+UUID.randomUUID().toString();
    private String contentType = "multipart/form-data; boundary="+boundary;
    private Vector<MultipartEntry> parts = new Vector();
    
    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void addPart(MultipartEntry multiPartEntry) {
        parts.add(multiPartEntry);
    }

    public long getContentLength() {
        long result = 0;
        int boundaryLength = boundary.length()+HttpConstants.CRLF.length();
        for (MultipartEntry entry : parts){
            result+=boundaryLength;
            result+=entry.getContentLength();
        }
        result+=boundaryLength+FINAL_BORDER.length();
        
        return result;
    }

    public void write(OutputStream output) {
        try {
            for (MultipartEntry entry : parts){
                try {
                    entry.writeTo(output, boundary);
                } catch (IOException ex) {
                    Logger.getLogger(MultipartContainer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             DataOutputStream dout = new DataOutputStream(output);
                   
            // write out the data
            dout.writeBytes(boundary+FINAL_BORDER);
        } catch (IOException ex) {
            Logger.getLogger(MultipartContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
