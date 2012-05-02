/*
 *  Description of JSONTextHelper
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 02.05.2012
 */
package sit.json;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class JSONTextHelper {

    public static String encodeText(String valueText) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < valueText.length(); i++) {
            char myChr = valueText.charAt(i);

            //Reference: http://www.unicode.org/versions/Unicode5.1.0/
            if ((myChr >= '\u0000' && myChr <= '\u001F') || (myChr >= '\u007F' && myChr <= '\u009F') || (myChr >= '\u2000' && myChr <= '\u20FF')) {
                String ss = Integer.toHexString(myChr);
                result.append("\\u");
                for (int k = 0; k < 4 - ss.length(); k++) {//add padding
                    result.append('0');
                }
                result.append(ss.toUpperCase());

            } else if (myChr == '\\') {
                result.append("\\\\");

            } else if (myChr == '\"') {
                result.append("\\\"");

            } else {
                result.append(myChr);
            }


        }
        return result.toString();
    }

    static String decodeText(String jsonText) {
        StringBuilder result = new StringBuilder();

        boolean escapeFlag = false;



        for (int i = 0; i < jsonText.length(); i++) {
            char myChr = jsonText.charAt(i);

            if ((myChr == '\\')) {
                if (escapeFlag) {
                    result.append("\\");
                    escapeFlag = false;
                    continue;
                }
                escapeFlag = true;
                continue;
            }//else
            if (escapeFlag) {
                if (myChr == 'u') { // now we should get 4 digits with the unicode number
                    try {
                        result.append((char) Integer.parseInt(jsonText.substring(i + 1, i + 1 + 4), 16)); // (..., 16) means HEX!!!
                        escapeFlag = false;
                        i+=4; //forward i by the four 
                        continue;
                    } catch (NumberFormatException ex) {
                        Logger.getLogger(JSONTextHelper.class.getName()).log(Level.INFO, null, ex);
                    } catch (IndexOutOfBoundsException ex) {
                        Logger.getLogger(JSONTextHelper.class.getName()).log(Level.INFO, null, ex);
                    }
                    
                }//else or in case of an exception
                escapeFlag = false;
            }
            result.append(myChr);
        }
        return result.toString();
    }
}
