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
