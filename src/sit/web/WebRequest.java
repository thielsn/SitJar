/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.util.Hashtable;
import java.util.Map.Entry;

/**
 *
 * @author thiel
 */
public class WebRequest {

    public String httpCommand = null;
    public String fname = null;
    public String param = null;
    public String body = null;

    public Hashtable<String,String> headerItems = null;

    @Override
    public String toString() {
        String result = httpCommand+" "+ fname;
        if (param != null) {
            result += "?"+ param;
        }        
        if (headerItems != null){
            result += "\nHeader:\n";
            for (Entry<String,String> headerEntry: headerItems.entrySet()){
                result += headerEntry.getKey()+": "+headerEntry.getValue()+"\n";
            }            
            result += "\n";
        }
        if (body != null){
            result += "\nBody:\n"+body+"\n----\n";
        }
        return result;
    }
}
