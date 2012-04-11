/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.HashTableSet;

/**
 *
 * @author thiel
 */
public class ServiceEndpoints {

    /**
     * Singleton instance
     */
    private static final ServiceEndpoints instance = new ServiceEndpoints();
    private HashTableSet<String, ServiceEndpoint> endpoints = new HashTableSet();

    private ServiceEndpoints() {
    }

    private ServiceEndpoint findEndpoint(String endpointName) {
        ServiceEndpoint result = endpoints.get(endpointName);
        if (result == null) {
            String myFname = endpointName.replaceAll("\\\\", "/");//for some strange reasons / seems to be transfered into \ //TODO - for windows only?
            result = endpoints.get(myFname);
        }
        return result;
    }

    public synchronized ServiceEndpoint getEndpoint(String endpointName) {
        if (endpointName == null) {
            return null;
        }
        //first try to find directly fitting endpoint
        ServiceEndpoint result = findEndpoint(endpointName);

        //in case this was not successful, we try to get a more generic one
        if (result == null) {
            try {
                if (endpointName.contains("/")) {
                    result = findEndpoint(endpointName.substring(0, endpointName.lastIndexOf("/") + 1));
                } else if (endpointName.contains("\\\\")) {
                    result = findEndpoint(endpointName.substring(0, endpointName.lastIndexOf("\\\\") + 1));
                }
            } catch (IndexOutOfBoundsException ex) {
                Logger.getLogger(ServiceEndpoints.class.getName()).log(Level.WARNING, "Error when trying to find general Service", ex);
            }

        }

        return result;
    }

    public synchronized void addEndpoint(ServiceEndpoint endpoint) {
        endpoints.add(endpoint);
    }

    public synchronized void removeEndpoint(String endpointName) {
        endpoints.remove(endpointName);
    }

    public synchronized static ServiceEndpoints getInstance() {
        return instance;
    }

    @Override
    public synchronized String toString(){

        String result = "";
        for (ServiceEndpoint entry : endpoints){
            result += entry.getKey() + " - " +entry.getEndpointName()+"\n";
        }
        return result;
    }
}
