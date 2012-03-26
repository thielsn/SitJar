/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.io.File;

class TestService extends ServiceEndpoint {

    public TestService(String endpointName) {
        super(endpointName);
    }

    @Override
    public String handleCall(WebRequest request) {
        return "Test worked at "+request;
    }
}

/**
 *
 * @author simon
 */
public class TestWebServer {

    public static void main(String[] args) {
        TestService ts = new TestService("test");
        ServiceEndpoints.getInstance().addEndpoint(ts);

        WebServer.getInstance().setRoot(new File("C:/Projekte/Transport_Bayer/www"));
        Thread webThread = new Thread(WebServer.getInstance());
        webThread.start();
    }
}
