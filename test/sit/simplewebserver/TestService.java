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
            //Logger.getLogger(TestService.class.getName()).log(Level.INFO, "reveived call:\n"+request);
            
            if (request.body!=null){
                new FileHelper().writeToFile("request_body", request.body);
            }
            
            if(request.contentType.isMultiPart()){
                MultipartContainer result = MultipartParser.parse(request.contentType.boundary, HttpConstants.DEFAULT_CHARSET, request.body);
                for (MultipartEntry entry : result){
                    if (entry.getType()==TYPES.BINARY){
                        
                        MPFileEntry fEntry = (MPFileEntry)entry;
                        if (fEntry.getFileName()!=null){
                            Logger.getLogger(TestService.class.getName()).log(Level.INFO, "received file:"+fEntry.getFileName());
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
