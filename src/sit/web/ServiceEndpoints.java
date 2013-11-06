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

/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 */
package sit.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.HashMapSet;

/**
 *
 * @author thiel
 */
public class ServiceEndpoints {

    /**
     * Singleton instance
     */
    private static final ServiceEndpoints instance = new ServiceEndpoints();
    private HashMapSet<String, ServiceEndpoint> endpoints = new HashMapSet();
    /**
     * in case at least one endpoint with isCatchAll == true is added this field is set
     * to true. This will make the getEndpoint less efficient, since also catchAll
     * endpoints are to be checked. Its recommended to keep the number of catchAll endpoints
     * limited.
     */
    private boolean hasCatchAllEndpoints = false;

    private ServiceEndpoints() {
    }

    private ServiceEndpoint findCatchAllEndpoint(String fname) {
        String subPath = fname;
        int slashIndex;
        while ((slashIndex = subPath.lastIndexOf("/"))>0){
            String myEndpoint  = subPath.substring(0,slashIndex);
            ServiceEndpoint result = endpoints.get(myEndpoint);
            if (result==null){
                result = endpoints.get(myEndpoint+"/");
            }
            if (result!=null){
                return result;
            }//else
            subPath = subPath.substring(0, slashIndex);
        }
        return null;

    }

    private ServiceEndpoint findEndpoint(String endpointName) {
        ServiceEndpoint result = endpoints.get(endpointName);
        if (result == null) {
            String myFname = endpointName.replaceAll("\\\\", "/");//for some strange reasons / seems to be transfered into \ //TODO - for windows only?
            result = endpoints.get(myFname);
            if (hasCatchAllEndpoints && (result == null)) { //check as well for catch all - if no more specific endpoint was found
                result = findCatchAllEndpoint(myFname);
            }
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
        if (endpoint.isCatchAll()) {
            hasCatchAllEndpoints = true;
        }
    }

    public synchronized void removeEndpoint(String endpointName) {
        endpoints.remove(endpointName);
    }

    public synchronized static ServiceEndpoints getInstance() {
        return instance;
    }

    @Override
    public synchronized String toString() {

        String result = "";
        for (ServiceEndpoint entry : endpoints) {
            result += entry.getKey() + " - " + entry.getEndpointName() + "\n";
        }
        return result;
    }
}
