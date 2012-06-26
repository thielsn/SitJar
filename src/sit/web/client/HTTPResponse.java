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


    
    public String call;
    public String message = "";
    /** http response code from the call */
    public int code = -1;    
    public final byte[] payload;
    public String reply = "";


    public HTTPResponse(String call, byte[] payload) {

        this.call = call;
        this.payload = payload;
    }

    

   

    @Override
    public String toString() {
        String result = "call:" + call;
        result += "\npayload:\n" + payload + "\ncode:" + code + "\nmessage:\n" + message + "\nreply:\n" + reply;
        return result;
    }

}
