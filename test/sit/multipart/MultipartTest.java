/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.multipart;

import java.io.File;
import sit.web.multipart.MPTextEntry;
import sit.web.multipart.MultipartContainer;
import sit.web.multipart.MultipartEntry;
import sit.web.multipart.TYPES;


/**
 *
 * @author simon
 */
public class MultipartTest {
      /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File myFile = new File("resource/picknick.jpg");
        
        MultipartContainer mpc = new MultipartContainer();
        mpc.addPart(new MPTextEntry(TYPES.TEXT, "text/plain", "filename"));
        
        
        
        
    }
}
