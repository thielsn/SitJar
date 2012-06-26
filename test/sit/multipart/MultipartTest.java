/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.multipart;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.web.MimeTypes;
import sit.web.client.HTTPResponse;
import sit.web.client.HttpHelper;
import sit.web.multipart.MPFileEntry;
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
        try {
            File myFile = new File("resources/picknick.jpg");
            
            MultipartContainer mpc = new MultipartContainer();
            mpc.addPart(new MPTextEntry(TYPES.TEXT, "text/plain", "filename", myFile.getName()));
            mpc.addPart(new MPFileEntry(TYPES.BINARY, MimeTypes.getMimeTypeFromFileName(myFile.getName()),"file", myFile));
            HTTPResponse response = new HttpHelper().postMulitPartContainer("localhost", 8080, "/testservice", mpc, false,"");
            Logger.getLogger(MultipartTest.class.getName()).log(Level.INFO, 
                    "response: "+response.toString());
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(MultipartTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MultipartTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
}
