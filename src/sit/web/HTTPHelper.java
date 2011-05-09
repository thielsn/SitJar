/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import sit.sstl.Pair;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class HTTPHelper {

    public final static String CRLF = "\r\n";
    public final static String CRLFCRLF = CRLF + CRLF;
    public final static int MAX_MESSAGE_SIZE = 2097152;

    public int getCRLFCRLFindex(String data){
        return data.indexOf(CRLFCRLF);
    }

    public String getHeader(String data){
        int myIndex = getCRLFCRLFindex(data);
        if (myIndex==-1){
            return null;
        }

        return data.substring(0,myIndex);
    }


    public String getHeader(InputStream is, WebBuffer buf) throws IOException{
        
        
        String result = null;
        String data = "";

        while ((buf.readFromInputStream(is)>0) && (result==null)){
            data += buf.toString();
            result = getHeader(data);
        }

        return result;

    }

    public Pair<String, String> getHeaderAndBody(InputStream is, WebBuffer buf) throws IOException, MessageTooLargeException{
        Pair<String, String> result = new Pair(null,null);


        String data = "";

        //get header
        while ((buf.readFromInputStream(is)>0) && (result.getA()==null)){
            data += buf.toString();
            int myIndex = getCRLFCRLFindex(data);
            if (myIndex!=-1){
                result.setA(data.substring(0,myIndex));
                //remaining part is reserved for the body
                if (data.length()>myIndex+CRLFCRLF.length()){
                    data = data.substring(myIndex+CRLFCRLF.length());
                }
            }
            checkMaxLenght(data);

        }
        //get body
        while ((buf.readFromInputStream(is)>0)){
            data += buf.toString();
            checkMaxLenght(data);
        }
        result.setB(data);
        
        return result;

        
    }

    private void checkMaxLenght(String data) throws MessageTooLargeException {
        if (data.length()>MAX_MESSAGE_SIZE){
            throw new MessageTooLargeException();
        }
    }

}
