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

/**
 *
 * @author simon
 */
public class MPTextEntry extends MultipartEntry {

    protected String payload;
    
    public MPTextEntry(TYPES type, String mimetype, String name, String payload) {
        super(type, mimetype, name);
        this.payload = payload;
    }
    

    @Override
    protected void writePartContentTo(OutputStream out) throws IOException {
        DataOutputStream output = new DataOutputStream(out);
        output.writeBytes(payload);
    }

    @Override
    public long getContentLengthOfContent() {
        return payload.length();
    }
    
}
