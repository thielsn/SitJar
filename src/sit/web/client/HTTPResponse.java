/*
 *  Description of HTTPResponse
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 09.04.2012
 */
package sit.web.client;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class HTTPResponse {


    public static final String[] dataPath = {"response", "data"};
    public static final String[] entryPath = {"response", "data", "entry"};
    public static final String[] statusPath = {"response", "meta", "status"};
    public String call;
    public boolean callFailed = false;
    /** http response code from the call */
    public int code = -1;
    public boolean isErrorMessage = false;
    public String message = "";
    public boolean noDataEntryFound = false;
    public boolean noDataFound = false;
    public final String payload;
    public String reply = "";


    public HTTPResponse(String call, String payload) {

        this.call = call;
        this.payload = payload;
    }

    public boolean hasError() {
        return callFailed || isErrorMessage || noDataEntryFound || noDataFound;
    }

   

    @Override
    public String toString() {
        String result = "call:" + call;
        result += "\npayload:\n" + payload + "\ncode:" + code + "\nmessage:\n" + message + "\nreply:\n" + reply;
        return result;
    }

}
