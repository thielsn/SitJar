/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.ByteBuilder;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class HTTPParser {
    
    
    public int getCRLFCRLFindex(ByteBuilder data) {
        return data.indexOf(HttpConstants.CRLFCRLF_BYTE);
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
        Logger.getLogger(HTTPParser.class.getName()).log(Level.WARNING, "connection timed out!");
        return false;
    }

    public HTTPMessage getHeaderAndBody(InputStream is, WebBuffer buf) throws IOException, MessageTooLargeException, HTTPParseException {
        HTTPMessage result = new HTTPMessage();

        ByteBuilder data = new ByteBuilder();
        buf.init();

        //get header
        while (proceedToRead(buf, is, (result.hasHeader()))) {

            data.append(buf.getBuffer(), buf.getReadBytes());
            int myIndex = getCRLFCRLFindex(data);
            if (myIndex != -1) {                                //true in case CRLFCRLF was finaly retrieved and is in data 
                                                                // (this can only be called once since after this result.hasHeader()==finished==true)
                result.setHeader(new String(data.subSequence(0, myIndex), HttpConstants.DEFAULT_CHARSET));
                //remaining part is reserved for the body
                if (data.size() > myIndex + HttpConstants.CRLFCRLF_BYTE.length) {
                    data = new ByteBuilder(data.subSequence(myIndex + HttpConstants.CRLFCRLF_BYTE.length)); //remove headerpart from data, but keep additional data read
                }else{
                    data = new ByteBuilder();
                }
            }
            checkMaxLenght(data);

        }

        //check for missing header caused e.g. by timeout or malformed http call
        if (!result.hasHeader()){
            Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "missing header received data (" + data.size() + "):"+data.toString()
                    +"\nread bytes:"+buf.getReadBytes());
            return null;
        }
        if (result.getWebRequest()==null){            
            throw new HTTPParseException("WebRequest==null! - data:"+data.toString());
        }

        Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "httpCommand:"+result.getWebRequest().httpCommand);
        if (result.getWebRequest().httpCommand.equalsIgnoreCase(HttpConstants.HTTP_COMMAND_POST) || result.getWebRequest().httpCommand.equalsIgnoreCase(HttpConstants.HTTP_COMMAND_PUT)) {

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
            while (proceedToRead(buf, is, (data.size()>=contentLength))) {
                data.append(buf.getBuffer(), buf.getReadBytes());
                checkMaxLenght(data);
            }
            result.getWebRequest().body = data.toByteArray();
            Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "read " + data.size() + " body data");
            Logger.getLogger(HTTPParser.class.getName()).log(Level.FINER, "body data:\n"+result.getWebRequest().body);
        }
        return result;


    }

    private void checkMaxLenght(ByteBuilder data) throws MessageTooLargeException {
        if (data.size() > HTTPMessage.MAX_MESSAGE_SIZE) {
            throw new MessageTooLargeException();
        }
    }
}
