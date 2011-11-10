/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.Pair;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class HTTPHelper {

    public int getCRLFCRLFindex(StringBuilder data) {
        return data.indexOf(HttpConstants.CRLFCRLF);
    }

    private boolean proceedToRead(WebBuffer buf, InputStream is, boolean finished) throws IOException {
        if (finished) {
            return false;
        }
        if (!buf.isMoreDataToRead()) {           
            return false;
        }
        if (buf.readFromInputStream(is) > -1) {
            return true;
        }

        
        return false;
    }

    public HTTPMessage getHeaderAndBody(InputStream is, WebBuffer buf) throws IOException, MessageTooLargeException {
        HTTPMessage result = new HTTPMessage();

        StringBuilder data = new StringBuilder("");
        buf.init();

        //get header
        while (proceedToRead(buf, is, (result.hasHeader()))) {

            data.append(buf.toString());
            int myIndex = getCRLFCRLFindex(data);
            if (myIndex != -1) {
                result.setHeader(data.substring(0, myIndex));
                //remaining part is reserved for the body
                if (data.length() > myIndex + HttpConstants.CRLFCRLF.length()) {
                    data=new StringBuilder(data.substring(myIndex + HttpConstants.CRLFCRLF.length()));
                }
            }
            checkMaxLenght(data);

        }
        Logger.getLogger(HTTPHelper.class.getName()).log(Level.FINE, "httpCommand:"+result.getWebRequest().httpCommand);
        if (result.getWebRequest().httpCommand.equalsIgnoreCase(HttpConstants.HTTP_COMMAND_POST)) {

            //retrieve content length field
            String contentLenghtStr = result.getWebRequest().headerItems.get(HttpConstants.HTTP_HEADER_FIELD_CONTENT_LENGTH);
            int contentLength = Integer.MAX_VALUE;
            if (contentLenghtStr!=null){
                try{
                    contentLength = Integer.parseInt(contentLenghtStr);
                }catch(NumberFormatException ex){
                    //ignore and assume maxint
                }
            }
            //get body
            while (proceedToRead(buf, is, (data.length()>=contentLength))) {
                data.append(buf.toString());
                checkMaxLenght(data);
            }
            result.getWebRequest().body = data.toString();
            Logger.getLogger(HTTPHelper.class.getName()).log(Level.FINE, "read " + data.length() + " body data");
        }
        return result;


    }

    private void checkMaxLenght(StringBuilder data) throws MessageTooLargeException {
        if (data.length() > HTTPMessage.MAX_MESSAGE_SIZE) {
            throw new MessageTooLargeException();
        }
    }
}
