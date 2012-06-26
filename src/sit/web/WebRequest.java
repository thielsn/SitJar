/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thiel
 */
public class WebRequest {

    public String httpCommand = null;
    public String fname = null;
    public String param = null;
    public byte[] body = null;

    public Hashtable<String,String> headerItems = null;

    
    
    
    
    public String toBriefString() {
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
            result += "\nBody: ("+body.length+" bytes)";
        }
        return result;
    }
    
    
    
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
            result += "\nBody:\n"+getBodyAsString()+"\n----\n";
        }
        return result;
    }
    public String getBodyAsString(){       
        return new String(body,HttpConstants.DEFAULT_CHARSET);       
    }
}
