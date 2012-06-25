/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.multipart;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import sit.web.WebBuffer;

/**
 *
 * @author simon
 */
public class MPFileEntry extends MultipartEntry {

    protected File file;

    public MPFileEntry(TYPES type, String mimetype, String name, File file) {
        super(type, mimetype, name);

        this.file = file;
        this.filename = file.getName();

    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        FileInputStream is = new FileInputStream(file);
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
}
