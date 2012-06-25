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

    public MPTextEntry(TYPES type, String mimetype, String name) {
        super(type, mimetype, name);
    }
    

    @Override
    public void writeTo(OutputStream out) throws IOException {
        
    }
    
}
