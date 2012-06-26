/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.multipart;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.web.HttpConstants;

/**
 *
 * @author simon
 */
public class MultipartContainer implements Iterable<MultipartEntry> {
    public static final String FINAL_BORDER = "--"+HttpConstants.CRLF;
    public static final String MULTIPART_CONTENT_TYPE_FORMDATA_BOUNDARY = HttpConstants.MULTIPART_MIME_TYPE
            +HttpConstants.SUB_FIELD_SEPARATOR+HttpConstants.BOUNDARY_CONTENT_TYPE_PREFIX;
    
    

    
    private final String boundary;
    private final String contentType; //content-type of multipart message must not have a charset field
    private Vector<MultipartEntry> parts = new Vector();

    public MultipartContainer() {
         boundary = "----------------"+UUID.randomUUID().toString();
         contentType = MULTIPART_CONTENT_TYPE_FORMDATA_BOUNDARY+boundary;
    }

    public MultipartContainer(String boundary) {
        this.boundary = boundary;
        contentType = MULTIPART_CONTENT_TYPE_FORMDATA_BOUNDARY+boundary;
    }
    
    
    
    
    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
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

    public Iterator<MultipartEntry> iterator() {
        return parts.iterator();
    }

    
}
