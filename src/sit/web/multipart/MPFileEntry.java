/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.multipart;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
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
