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

/**
 *
 * @author simon
 */
public class MultipartContainer {
    
    /*
    POST /path/to/script.php HTTP/1.0
    Host: example.com
    Content-type: multipart/form-data, boundary=AaB03x
    Content-Length: $requestlen

--AaB03x */
    
    private String contentType = "multipart/form-data; boundary=----------------"+UUID.randomUUID().toString();
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
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void write(OutputStream output) {
        for (MultipartEntry entry : parts){
            try {
                entry.writeTo(output);
            } catch (IOException ex) {
                Logger.getLogger(MultipartContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}
