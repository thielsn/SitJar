/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.multipart;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.io.FileHelper;
import sit.web.ServiceEndpoint;
import sit.web.WebRequest;

/**
 *
 * @author simon
 */
public class MultiPartService extends ServiceEndpoint{

    public MultiPartService(String endpointName, boolean catchAll) {
        super(endpointName, catchAll);
    }

    
    @Override
    public String handleCall(WebRequest request) {
        try {
            Logger.getLogger(MultiPartService.class.getName()).log(Level.INFO, "reveived call:\n"+request);
            new FileHelper().writeToFile("request_body", request.body);
            return "OK";
        } catch (IOException ex) {
            Logger.getLogger(MultiPartService.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        
    }
    
}
