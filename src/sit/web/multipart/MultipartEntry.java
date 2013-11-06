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
    
    public long getContentLength(int boundaryLength){
        
        return boundaryLength
                +HttpConstants.CRLF_BYTE.length
                +getHeader().length()
                +getContentLengthOfContent()
                +HttpConstants.CRLF_BYTE.length;
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
        output.writeBytes(boundary);
        output.write(HttpConstants.CRLF_BYTE);
        output.writeBytes(getHeader());

        writePartContentTo(out);
        output.write(HttpConstants.CRLF_BYTE);
    }

    protected abstract void writePartContentTo(OutputStream out) throws IOException;

    public TYPES getType() {
        return type;
    }

    public String getFileName() {
        return filename;
    }
}
