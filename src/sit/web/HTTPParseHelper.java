/*
 *  Description of HTTPParseHelper
 * 
 *  @author Simon Thiel
 *  @version $Revision: $
 *  @date 26.06.2012
 */
package sit.web;

import java.util.HashMap;

/**
 * HTTPParseHelper
 * 
 */
public class HTTPParseHelper {
    
    public static String getValueIfExists(String prefix, String value){
        if (value.toLowerCase().startsWith(prefix.toLowerCase())){            
            return value.substring(prefix.length());
        }
        return null;
    } 
    
  public static HashMap<String, String> parseAndFillFittingValues(String[] prefixes, String[] values){
        HashMap<String, String> result = new HashMap();
        for (String value : values){
            for (String prefix : prefixes){
                String realValue = getValueIfExists(prefix, value);
                if (realValue!=null){
                    result.put(prefix, realValue);
                }
            }
        }
        return result;
    }
}
