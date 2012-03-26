/**
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */

package sit.web;

import sit.sstl.ObjectWithKey;

/**
 *
 * @author thiel
 */
public abstract class ServiceEndpoint implements ObjectWithKey<String>{

    protected String endpointName = "";

    public ServiceEndpoint(String endpointName){
        this.endpointName = endpointName;
    }

    /**
     * @return the endpointName
     */
    public String getEndpointName() {
        return endpointName;
    }

    /**
     * @param endpointName the endpointName to set
     */
    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public String getKey() {
        return endpointName;
    }

    public abstract String handleCall(WebRequest request);

    public String getContentType() {
        return "text/html";
    }

}
