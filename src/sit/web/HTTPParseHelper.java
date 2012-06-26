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
            String result = value.substring(prefix.length());
            if (result.isEmpty()){
                return result;
            }
            //remove quotes if any
            if (result.equals("\"\"")){
                return "";
            }
            if (result.charAt(0)=='"' && result.charAt(result.length()-1)=='"'){
                result = result.substring(1, result.length()-1);
            }
            return result;
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
