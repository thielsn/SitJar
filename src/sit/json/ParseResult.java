/*
 *  Description of ParseResult
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 26.03.2012
 */
package sit.json;

/**
 *
 * @author simon
 */
public class ParseResult {

    JSONObject object = null;
    String value = null;
    boolean hasQuotes = false;
    int endPosition = -1;
}
