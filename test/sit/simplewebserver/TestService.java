/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.simplewebserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.io.FileHelper;
import sit.web.HttpConstants;
import sit.web.ServiceEndpoint;
import sit.web.WebRequest;
import sit.web.multipart.MultipartParser;

/**
 *
 * @author simon
 */
public class TestService extends ServiceEndpoint{

    public TestService(String endpointName, boolean catchAll) {
        super(endpointName, catchAll);
    }

    
    @Override
    public String handleCall(WebRequest request) {
        try {
            Logger.getLogger(TestService.class.getName()).log(Level.INFO, "reveived call:\n"+request);
            new FileHelper().writeToFile("request_body", request.body);
            request.headerItems.get(HttpConstants.CONTENT_TYPE_TAG)
            MultipartParser.parse(endpointName, null, payload)
            
            return "OK";
        } catch (IOException ex) {
            Logger.getLogger(TestService.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
        
    }
    
}
