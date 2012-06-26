/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.simplewebserver;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.io.FileHelper;
import sit.web.HttpConstants;
import sit.web.ServiceEndpoint;
import sit.web.WebRequest;
import sit.web.multipart.MPFileEntry;
import sit.web.multipart.MultipartContainer;
import sit.web.multipart.MultipartEntry;
import sit.web.multipart.MultipartParser;
import sit.web.multipart.TYPES;

/**
 *
 * @author simon
 */
public class TestService extends ServiceEndpoint{

    public TestService(String endpointName, boolean catchAll) {
        super(endpointName, catchAll);
    }

    
    @Override
    public byte[] handleCall(WebRequest request) {
        try {
            Logger.getLogger(TestService.class.getName()).log(Level.INFO, "reveived call:\n"+request);
            new FileHelper().writeToFile("request_body", request.body);
            
            if(request.contentType.isMultiPart()){
                MultipartContainer result = MultipartParser.parse(request.contentType.boundary, HttpConstants.DEFAULT_CHARSET, request.body);
                for (MultipartEntry entry : result){
                    if (entry.getType()==TYPES.BINARY){
                        
                        MPFileEntry fEntry = (MPFileEntry)entry;
                        if (fEntry.getFileName()!=null){
                            
                            new FileHelper().writeToFile((new File(entry.getFileName())).getName(), fEntry.getFileContent());
                        }
                    }
                }
            }
            
            
            return "OK".getBytes();
        } catch (IOException ex) {
            Logger.getLogger(TestService.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage().getBytes();
        }
        
    }
    
}
