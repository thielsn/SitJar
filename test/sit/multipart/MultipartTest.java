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
            HTTPResponse response = new HttpHelper().postMultiPartContainer("localhost", 8080, "/testservice", mpc, false,"");
            Logger.getLogger(MultipartTest.class.getName()).log(Level.INFO, 
                    "response: "+response.toString());
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(MultipartTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MultipartTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
}
