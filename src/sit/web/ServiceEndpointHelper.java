/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
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

    public String decode(String urlString) throws UnsupportedEncodingException{
        String result = java.net.URLDecoder.decode(urlString, "UTF-8");
        result = result.replaceAll("%20", " ");
        result = result.replaceAll("%2C", ",");
        result = result.replaceAll("%40", "@");
        result = result.replaceAll("%2B", "+");
        result = result.replaceAll("%3A", ":");
        result = result.replaceAll("%2F", "/");
        return result;
    }
    
    public ServiceComponents extractNameValues(String request) throws UnsupportedEncodingException {

        ServiceComponents result = new ServiceComponents();

        if (request == null){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "request == null !");
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


    public String xmlize(String rootTag, Vector<Pair<String, String>> nv){
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
    public String getSubPath(String path, String basePath){
        if (!path.startsWith(basePath)){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "path:"+path+" is not starting with basePath:"+basePath);
            return null;
        }
        if (path.length()==basePath.length()){ //they are equal
            return "";
        }
        return path.substring(basePath.length());
    }

    public String replaceBackSlashes(String source){
        return source.replaceAll("\\\\", "/");
    }
}
