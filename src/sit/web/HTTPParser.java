/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.ByteBuilder;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class HTTPParser {
    public static final String CONTINUE_100 = "100-continue";
    public static final String EXPECT_HEADER_FIELD = "Expect:";
    public static final String HTTP_100__CONTINUE = "HTTP/1.1 100 Continue\r\n\r\n";

    public int getCRLFCRLFindex(ByteBuilder data) {
        return data.indexOf(HttpConstants.CRLFCRLF_BYTE);
    }

    private boolean proceedToRead(WebBuffer buf, InputStream is, boolean finished) throws IOException {
        if (finished) {
            //Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "finished");
            return false;
        }
        if (!buf.isMoreDataToRead()) {
           // Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "no more data");
            return false;
        }
        if (buf.readFromInputStream(is) > -1) {
            //Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "more data found");
            return true;
        }
        Logger.getLogger(HTTPParser.class.getName()).log(Level.WARNING, "connection timed out!");
        return false;
    }

    public HTTPMessage getHeaderAndBody(InputStream is, WebBuffer buf, PrintStream ps) throws IOException, MessageTooLargeException, HTTPParseException {
        HTTPMessage result = new HTTPMessage();

        ByteBuilder data = new ByteBuilder();
        buf.init();

        //get header
        while (proceedToRead(buf, is, (result.hasHeader()))) {

            data.append(buf.getBuffer(), buf.getReadBytes());
            int myIndex = getCRLFCRLFindex(data);
            if (myIndex != -1) {                                //true in case CRLFCRLF was finaly retrieved and is in data 
		// (this can only be called once since after this result.hasHeader()==finished==true)
                result.setHeader(new String(data.subSequence(0, myIndex)));
                //remaining part is reserved for the body
                if (data.size() > myIndex + HttpConstants.CRLFCRLF_BYTE.length) {
                    data = new ByteBuilder(data.subSequence(myIndex + HttpConstants.CRLFCRLF_BYTE.length)); //remove headerpart from data, but keep additional data read
                } else {
                    data = new ByteBuilder();
                }
            }
            checkMaxLenght(data);

        }

        //check for missing header caused e.g. by timeout or malformed http call
        if (!result.hasHeader()) {
            Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "missing header received data (" + data.size() + "):" + data.toString()
                    + "\nread bytes:" + buf.getReadBytes());
            return null;
        }
        if (result.getWebRequest() == null) {
            throw new HTTPParseException("WebRequest==null! - data:" + data.toString());
        }

        Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "httpCommand:" + result.getWebRequest().httpCommand);
        if (result.getWebRequest().httpCommand.equalsIgnoreCase(HttpConstants.HTTP_COMMAND_POST) 
                || result.getWebRequest().httpCommand.equalsIgnoreCase(HttpConstants.HTTP_COMMAND_PUT)) {

            //handle 100 continue issue // 100 (Continue)   "HTTP/1.1 100 Continue"
            if (CONTINUE_100.equalsIgnoreCase(result.getWebRequest().headerItems.get(EXPECT_HEADER_FIELD.toUpperCase()))){
                ps.print(HTTP_100__CONTINUE);
                ps.flush();
                Logger.getLogger(HTTPParser.class.getName()).log(Level.INFO, "sent: "+"HTTP/1.1 100 Continue\r\n\r\n");
            }
            
            //retrieve content length field
            String contentLengthStr = result.getWebRequest().headerItems.get(HttpConstants.HTTP_HEADER_FIELD_CONTENT_LENGTH.toUpperCase());
            long contentLength = Long.MAX_VALUE;
            if (contentLengthStr != null) {
                try {
                    contentLength = Long.parseLong(contentLengthStr);
                } catch (NumberFormatException ex) {
                    //ignore and assume maxlong
                }
            }
            Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "contentLength:" + contentLengthStr + "(" + contentLength + ")");
            //get body
            while (proceedToRead(buf, is, (data.size() >= contentLength))) {
                data.append(buf.getBuffer(), buf.getReadBytes());
                checkMaxLenght(data);
            }
            result.getWebRequest().body = data.toByteArray();
            Logger.getLogger(HTTPParser.class.getName()).log(Level.FINE, "read " + data.size()+ " body data");
            Logger.getLogger(HTTPParser.class.getName()).log(Level.FINER, "body data:\n" + result.getWebRequest().body);
        }
        return result;


    }

    private void checkMaxLenght(ByteBuilder data) throws MessageTooLargeException {
        if (data.size() > HTTPMessage.MAX_MESSAGE_SIZE) {
            throw new MessageTooLargeException();
        }
    }
}
