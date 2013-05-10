/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author simon
 */
public class HttpConstantsHelper  implements HttpConstants{
    private final static Map<Integer, String> CODE_MESSAGE_MAP = new HashMap();
    
    {
        //200
        CODE_MESSAGE_MAP.put(HTTP_OK, "OK");
        CODE_MESSAGE_MAP.put(HTTP_CREATED, "Created");
        CODE_MESSAGE_MAP.put(HTTP_NOT_AUTHORITATIVE, "Non-Authoritative Information");
        CODE_MESSAGE_MAP.put(HTTP_NO_CONTENT, "No Content");
        CODE_MESSAGE_MAP.put(HTTP_RESET, "Reset Content");
        CODE_MESSAGE_MAP.put(HTTP_PARTIAL, "Partial Content");
        //300
        CODE_MESSAGE_MAP.put(HTTP_MULT_CHOICE, "Multiple Choices");
        CODE_MESSAGE_MAP.put(HTTP_MOVED_PERM, "Moved Permanently");
        CODE_MESSAGE_MAP.put(HTTP_SEE_OTHER, "See Other");
        CODE_MESSAGE_MAP.put(HTTP_NOT_MODIFIED, "Not Modified");        
        CODE_MESSAGE_MAP.put(HTTP_USE_PROXY, "Use Proxy");
        CODE_MESSAGE_MAP.put(HTTP_TEMPORARY_REDIRECT, "Temporary Redirect");
        //400
        CODE_MESSAGE_MAP.put(HTTP_BAD_REQUEST, "Bad Request");
        CODE_MESSAGE_MAP.put(HTTP_UNAUTHORIZED, "Unauthorized");
        CODE_MESSAGE_MAP.put(HTTP_PAYMENT_REQUIRED, "Payment Required");
        CODE_MESSAGE_MAP.put(HTTP_FORBIDDEN, "Forbidden");        
        CODE_MESSAGE_MAP.put(HTTP_NOT_FOUND, "Not Found");
        CODE_MESSAGE_MAP.put(HTTP_BAD_METHOD, "Method Not Allowed");
        CODE_MESSAGE_MAP.put(HTTP_NOT_ACCEPTABLE, "Not Acceptable");
        CODE_MESSAGE_MAP.put(HTTP_PROXY_AUTH, "Proxy Authentication Required");
        CODE_MESSAGE_MAP.put(HTTP_CLIENT_TIMEOUT, "Request Time-out");
        CODE_MESSAGE_MAP.put(HTTP_CONFLICT, "Conflict");
        CODE_MESSAGE_MAP.put(HTTP_GONE, "Gone");
        CODE_MESSAGE_MAP.put(HTTP_LENGTH_REQUIRED, "Length Required");
        CODE_MESSAGE_MAP.put(HTTP_PRECON_FAILED, "Precondition Failed");
        CODE_MESSAGE_MAP.put(HTTP_ENTITY_TOO_LARGE, "Request Entity Too Large");
        CODE_MESSAGE_MAP.put(HTTP_REQ_TOO_LONG, "Request-URL Too Long");
        CODE_MESSAGE_MAP.put(HTTP_UNSUPPORTED_TYPE, "Unsupported Media Type");
        //TODO some messages are missing
        CODE_MESSAGE_MAP.put(HTTP_TOO_MANY_REQUESTS, "Too Many Requests");
        //500
        CODE_MESSAGE_MAP.put(HTTP_SERVER_ERROR, "Internal Server Error");
        CODE_MESSAGE_MAP.put(HTTP_INTERNAL_ERROR, "Not Implemented");
        CODE_MESSAGE_MAP.put(HTTP_BAD_GATEWAY, "Bad Gateway");
        CODE_MESSAGE_MAP.put(HTTP_UNAVAILABLE, "Service Unavailable");
        CODE_MESSAGE_MAP.put(HTTP_GATEWAY_TIMEOUT, "Gateway Time-out");
        CODE_MESSAGE_MAP.put(HTTP_VERSION, "HTTP Version not supported");
        
        
  
    }
    
    public static String getHTTPCodeMessage(int returnCode) {
        return CODE_MESSAGE_MAP.get(returnCode);
        
    }
}
