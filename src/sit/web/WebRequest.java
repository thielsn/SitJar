/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.util.Hashtable;

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
        String result = fname;
        if (param != null) {
            result += "?"+ param;
        }
        if (body != null){
            result += "\n----\n"+body;
        }
        return result;
    }
}
