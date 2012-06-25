/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
