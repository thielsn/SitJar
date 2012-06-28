/*
 *  Description of HTTPResponse
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 09.04.2012
 */
package sit.web.client;

import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.web.HttpConstants;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class HTTPResponse {


    
    public String call;
    public String message = "";
    public Charset charset;
    /** http response code from the call */
    public int code = -1;    
    public final byte[] payload;
    public String reply = "";
    


    public HTTPResponse(String call, byte[] payload, Charset charset) {

        this.call = call;
        this.charset = charset;
        this.payload = payload;
    }

    

   public String getPayloadAsString(){
       if (!charset.equals(Charset.defaultCharset())){
            Logger.getLogger(HTTPResponse.class.getName()).log(Level.WARNING, "unexpected charset: "+charset+ " should be "+ Charset.defaultCharset());
            //throw new RuntimeException("other charsets than "+Charset.defaultCharset()+" not allowed in this implementation! charset:"+charset);
       }
       return new String(payload);
   }

    @Override
    public String toString() {
        String result = "call:" + call;
        result += "\npayload:\n" + getPayloadAsString() + "\ncode:" + code + "\nmessage:\n" + message + "\nreply:\n" + reply;
        return result;
    }

}
