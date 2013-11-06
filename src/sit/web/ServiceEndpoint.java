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

/**
 *  @author Simon Thiel <simon.thiel at gmx.de>
 */

package sit.web;

import java.nio.charset.Charset;
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
    public abstract byte[] handleCall(WebRequest request);

    /**
     * override this for returning different mime-type (is granted to get called after handle call by the web-server)
     * @return 
     */
    public String getMimeType() {
        return HttpConstants.DEFAULT_MIME_TYPE;
    }
    
    /**
     * to be overridden by extending classes
     * specifies Charset to be used for reply set to null in case no charset field should be sent 
     * 
     * @return 
     */
    public Charset getCharSet() {
        return Charset.defaultCharset();
    }
    
    
    /**
     * returns full set content type as required by HTTP-header field Content-Type
     * previous inherited implementations of getContentType should now override <code>getMimeType()</code>
     * @return 
     */
    private String getContentType() {
        String result = getMimeType();
        if (getCharSet()!=null){
            result += HttpConstants.SUB_FIELD_SEPARATOR+HttpConstants.CHARSET_CONTENT_TYPE_TAG+getCharSet().name();
         }
        return result;
    }
    
    /**
     * replaces getContentType to enforce changes in the overriding classes (previously getContentType only returned the mimetype)
     * @return 
     */
    protected String getContentTypeAsString(){
       return getContentType(); 
    }

}
