/*
 *  Description of ExceptionHelper
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 04.05.2012
 */
package sit.tools;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class ExceptionHelper {

    public static String stackTraceToString(Exception ex, String message) {
        StringBuilder errMessage = new StringBuilder();
        errMessage.append(message).append(": ").append(ex.getMessage()).append(":\n");

        for (StackTraceElement line : ex.getStackTrace()) {
            errMessage.append(line.toString()).append("\n");
        }
        return errMessage.toString();
    }
}
