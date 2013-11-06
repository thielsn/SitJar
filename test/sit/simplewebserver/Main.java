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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import sit.web.ServiceEndpoints;
import sit.web.WebServer;

/**
 *
 * @author thiel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // load logging property file propertiy
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream(new File("resources/simplewebserver.logging.properties")));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
  
        WebServer.getInstance().setRoot(new File("/"));
        WebServer.getInstance().setPermitDirectoryListing(true);
        WebServer.getInstance().setPort(8080);
        
        ServiceEndpoints.getInstance().addEndpoint(new TestService("testservice", true));
        
        Thread webThread = new Thread(WebServer.getInstance());
        webThread.start();
        
    }

}
