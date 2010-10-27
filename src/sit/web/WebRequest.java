/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web;

/**
 *
 * @author thiel
 */
public class WebRequest {

    public String fname = null;
    public String param = null;

    @Override
    public String toString() {
        String result = fname;
        if (param != null) {
            result += "?"+ param;
        }
        return result;
    }
}
