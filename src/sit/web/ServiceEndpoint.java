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

    /**
     * in case an endpoint is flagged as catchAll == true, an endpoint a/b
     * will be found for all a/b/[c/d/e/....]
     */
    protected boolean catchAll = false;

    public ServiceEndpoint(String endpointName){
        this.endpointName = endpointName;
    }

    public ServiceEndpoint(String endpointName, boolean catchAll){
        this.endpointName = endpointName;
        this.catchAll = catchAll;

    }

    public boolean isCatchAll() {
        return catchAll;
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

    /**
     * reentrant function returning the content for the request
     * @param request
     * @return 
     */
    public abstract String handleCall(WebRequest request);

    public String getContentType() {
        return HttpConstants.DEFAULT_MIME_TYPE;
    }

}
