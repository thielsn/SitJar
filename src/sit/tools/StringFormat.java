/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.tools;

/**
 *
 * @author simon
 */
public class StringFormat {

    /**
     * Trims the quotes. <p> For example, <ul> <li>("a.b") => a.b <li>("a.b) =>
     * "a.b <li>(a.b") => a.b" </ul>
     *
     * @param value the string may have quotes
     * @return the string without quotes
     */
    public static String trimQuotes(String value) {
        if (value == null) {
            return value;
        }

        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }

        return value;
    }
    
    
}
