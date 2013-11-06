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
    public static final String PRE_BOUNDARY = "--";
    public static final String FINAL_BORDER = PRE_BOUNDARY+HttpConstants.CRLF;
    public static final String MULTIPART_CONTENT_TYPE_FORMDATA_BOUNDARY = HttpConstants.MULTIPART_MIME_TYPE
            +HttpConstants.SUB_FIELD_SEPARATOR+HttpConstants.BOUNDARY_CONTENT_TYPE_PREFIX;
    
    
    

    
    private final String pure_boundary;
    private final String part_boundary;
    private final String contentType; //content-type of multipart message must not have a charset field
    private Vector<MultipartEntry> parts = new Vector();

    public MultipartContainer() {
         pure_boundary = UUID.randomUUID().toString();
         part_boundary=PRE_BOUNDARY+pure_boundary;
         contentType = MULTIPART_CONTENT_TYPE_FORMDATA_BOUNDARY+pure_boundary;
    }

    public MultipartContainer(String pure_boundary) {
        this.pure_boundary = pure_boundary;
        part_boundary=PRE_BOUNDARY+pure_boundary;
        contentType = MULTIPART_CONTENT_TYPE_FORMDATA_BOUNDARY+pure_boundary;
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
        
        for (MultipartEntry entry : parts){           
            result+=entry.getContentLength(part_boundary.length());
        }
        result+=part_boundary.length()+FINAL_BORDER.length();
        
        return result;
    }

    public void write(OutputStream output) {
        try {
            for (MultipartEntry entry : parts){
                try {
                    entry.writeTo(output, part_boundary);
                } catch (IOException ex) {
                    Logger.getLogger(MultipartContainer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             DataOutputStream dout = new DataOutputStream(output);
                   
            // write out the data
            dout.writeBytes(part_boundary+FINAL_BORDER);
        } catch (IOException ex) {
            Logger.getLogger(MultipartContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Iterator<MultipartEntry> iterator() {
        return parts.iterator();
    }

    /**
     * @return the part_boundary
     */
    public String getPart_boundary() {
        return part_boundary;
    }

    
}
