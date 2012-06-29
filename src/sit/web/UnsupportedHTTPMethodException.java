/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */

package sit.web;

/**
 *
 * @author thiel
 */
public class UnsupportedHTTPMethodException extends Exception {

    /**
     * Creates a new instance of <code>UnsupportedHTTPMethod</code> without detail message.
     */
    public UnsupportedHTTPMethodException() {
    }


    /**
     * Constructs an instance of <code>UnsupportedHTTPMethod</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UnsupportedHTTPMethodException(String msg) {
        super(msg);
    }
}
