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

import java.io.UnsupportedEncodingException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.Pair;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class ServiceEndpointHelper {

    public static String decode(String urlString) throws UnsupportedEncodingException{
        String result = java.net.URLDecoder.decode(urlString, "UTF-8");
        result = result.replaceAll("%20", " ");
        result = result.replaceAll("%2C", ",");
        result = result.replaceAll("%40", "@");
        result = result.replaceAll("%2B", "+");
        result = result.replaceAll("%3A", ":");
        result = result.replaceAll("%2F", "/");
        return result;
    }
    
    public static ServiceComponents extractNameValues(String request) throws UnsupportedEncodingException {

        ServiceComponents result = new ServiceComponents();

        if (request == null){
            Logger.getLogger(ServiceEndpointHelper.class.getName()).log(Level.SEVERE, "request == null !");
            return result;
        }

        String requestSt = request;

        String[] fields = requestSt.split("&");
        for (String field : fields) {
            String[] nameVal = field.split("=");
            if (nameVal.length > 1) {
                result.add(new ServiceComponent(decode(nameVal[0]), decode(nameVal[1])));
            }

        }
        return result;
    }
    
    public static ServiceComponent getServiceComponent(ServiceComponents serviceComponents, String key){
        for (ServiceComponent component: serviceComponents){
            if (component.getA().equals(key)){
                return component;
            }
        }
        return null;
    }
    
    
    public static String getURLParamForKeyFromWR(WebRequest wr, String key) throws UnsupportedEncodingException{
        if (wr==null || wr.param==null){
            return null;
        }
        ServiceComponents urlParams = extractNameValues(wr.param);
        ServiceComponent sc = getServiceComponent(urlParams, key);
        if (sc!=null){
            return sc.getB();
        }
        return null;
        
    }
    

    public static String xmlize(String rootTag, Vector<Pair<String, String>> nv){
        String result ="<"+rootTag+">";

        for (Pair<String, String> field : nv){
            result += "<"+field.getA()+">";
            result += field.getB();
            result += "</"+field.getA()+">";
        }

        return result+"</"+rootTag+">";
    }

    /**
     * returns subPath [c/d/e/...] from path a/b/[c/d/e/...] with basePath a/b/
     * @param path
     * @param basePath
     * @return
     */
    public static String getSubPath(String path, String basePath){
        if (!path.startsWith(basePath)){
            Logger.getLogger(ServiceEndpointHelper.class.getName()).log(Level.SEVERE,
                    "path:"+path+" is not starting with basePath:"+basePath);
            return null;
        }
        if (path.length()==basePath.length()){ //they are equal
            return "";
        }
        return path.substring(basePath.length());
    }

    public static String replaceBackSlashes(String source){
        return source.replaceAll("\\\\", "/");
    }
}
