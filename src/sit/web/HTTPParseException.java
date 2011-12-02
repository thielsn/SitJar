/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.web;

/**
 *
 * @author thiel
 */
public class HTTPParseException extends Exception {

    /**
     * Creates a new instance of <code>UnsupportedHTTPMethod</code> without detail message.
     */
    public HTTPParseException() {
    }


    /**
     * Constructs an instance of <code>UnsupportedHTTPMethod</code> with the specified detail message.
     * @param msg the detail message.
     */
    public HTTPParseException(String msg) {
        super(msg);
    }
}
