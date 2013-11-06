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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import sit.web.WebBuffer;

/**
 *
 * @author simon
 */
public class MPFileEntry extends MultipartEntry {

    protected File file;
    protected byte[] fileContent;

    public MPFileEntry(TYPES type, String mimetype, String name, File file) {
        super(type, mimetype, name);

        this.file = file;
        this.filename = file.getName();
        this.fileContent = null;

    }

    public MPFileEntry(TYPES type, String mimetype, String name, String fileName, byte[] content) {
        super(type, mimetype, name);

        this.filename = fileName;
        this.fileContent = content;
    }

    @Override
    protected void writePartContentTo(OutputStream out) throws IOException {

        InputStream is;

        if (fileContent != null) { // if we have the content in the storage use this
            is = new ByteArrayInputStream(fileContent);
        } else {
            is = new FileInputStream(file);
        }
        WebBuffer buf = new WebBuffer();
        PrintStream pOs = new PrintStream(out);
        try {
            int n;
            while ((n = buf.readFromInputStream(is)) > 0) {
                buf.writeToOutStream(pOs, 0, n);
            }
        } finally {
            is.close();
        }
    }

    @Override
    public long getContentLengthOfContent() {
        if (fileContent!=null){
            return fileContent.length;
        }
        return file.length();
    }
    
    public byte[] getFileContent(){
        return fileContent;
    }
    
}
