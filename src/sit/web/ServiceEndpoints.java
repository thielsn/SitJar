/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.web;


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



    private ServiceEndpoints(){
    }

    public synchronized  ServiceEndpoint getEndpoint(String binding){
        if (binding==null){
            return null;
        }
        return endpoints.get(binding);
    }

    public synchronized void addEndpoint(ServiceEndpoint endpoint){
        endpoints.add(endpoint);
    }

    public synchronized static ServiceEndpoints getInstance() {
        return instance;
    }

}
