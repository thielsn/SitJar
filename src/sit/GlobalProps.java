/*
 * GlobalProps.java
 *
 * Created on 25. Maerz 2006, 21:40
 */

package sit;

/**
 *
 * @author  thiel
 */
public class GlobalProps extends sit.tools.DefaultProps {

        
    protected void initDefaults(){
        fileName="GlobalProps.properties";
        defaultHeader = "GlobalProps_Header";
        header = "GlobalProps_Header";
        
        defaults = new java.util.Properties();
        //defaults.setProperty("outDir", "");
        //defaults.setProperty("wsdlFileName", "");
        defaults.setProperty("dynamic", "false");
        defaults.setProperty("REMOTE_ADDRESS", "http://137.251.45.130:80/");//SIT
        
        defaults.setProperty("LOCAL_ADDRESS", "http://84.57.201.137:9090");
        defaults.setProperty("RECEIVER_PORT", "9090");
        defaults.setProperty("TIMEOUT_RECEIVER", "30");
        defaults.setProperty("USE_KIK", "true");
        defaults.setProperty("receiverActive", "false");        
        defaults.setProperty("LOG_DIRECTORY", "log");
        
        
        defaults.setProperty("KIKNameSpaceURI", "http://webservice-kompass.de/names/routing");
        defaults.setProperty("KIKServiceName", "kik/routing/ReceiverService");
        defaults.setProperty("KIKServiceAddress", "http://62.206.32.212:8080/");
        //defaults.setProperty("KIKServiceEndpoint", "http://62.206.32.212:8080/"
        //+"kik/routing/ReceiverService");
        
        
        defaults.setProperty("VisDir", "log");
        defaults.setProperty("VisHTMLFile", "index.html");
                
        defaults.setProperty("storageDirectory", "log/");
        defaults.setProperty("storageFile", "processes.xml");        
       
        
    }
        
        
    
}
