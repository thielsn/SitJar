package sit.simplewebserver;

import java.io.File;
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
  
        WebServer.getInstance().setRoot(new File("/"));
        WebServer.getInstance().setPermitDirectoryListing(true);
        WebServer.getInstance().setPort(8080);
        
        ServiceEndpoints.getInstance().addEndpoint(new TestService("testservice", true));
        
        Thread webThread = new Thread(WebServer.getInstance());
        webThread.start();
        
    }

}
