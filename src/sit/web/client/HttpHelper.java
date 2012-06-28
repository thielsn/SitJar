/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.client;

import java.io.DataInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import sit.web.HttpConstants;
import sit.web.MimeTypes;
import sit.web.multipart.MultipartContainer;

/**
 *
 * @author simon
 */

import sit.tools.Base64;
public class HttpHelper {

    public static String getBase64UserNamePwdToken(String username, String password){
        return Base64.encodeBytes((username+":"+password).getBytes());
    }

    public static String encodeString(String myString) {
        if (myString == null) {
            throw new NullPointerException("encodeString: myString == null!");
        }
        try {
            return java.net.URLEncoder.encode(myString, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError("UTF-8 not supported");
        }
    }

    public static String decodeString(String myString) {
        if (myString == null) {
            throw new NullPointerException("decodeString: myString == null!");
        }
        try {
            return java.net.URLDecoder.decode(myString, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError("UTF-8 not supported");
        }
    }

    public static URL getURL(String host, int port, String path, boolean isHTTPS)
            throws MalformedURLException {

        String myUrl = isHTTPS ? "https" : "http";
        myUrl += "://" + host + ":" + port + path;

        return new URL(myUrl);
    }

    public HttpHelper() {
        HTTPUrlConnectionHelper.initAllTrustingManager("TLS"); //or "SSL"
    }

    public HttpHelper(String sslContext) {
        HTTPUrlConnectionHelper.initAllTrustingManager(sslContext);
    }
    
    /**
 * 
 * convenient call, sets charSet automatically to <code>Charset.defaultCharset()</code>
 * @param method
 * @param host
 * @param port
 * @param path
 * @param payload
 * @param mimeType mimetype as string e.g. "application/json" will be added to the content type of the http call
 * @param isHTTPS
 * @param unamePword64
 * @return
 * @throws MalformedURLException
 * @throws ProtocolException
 * @throws IOException 
 */
    public HTTPResponse doHTTPRequest(String method, String host, int port, String path, String payload, String mimeType,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, ProtocolException, IOException {
        
        
        return doHTTPRequest(method, host, port, path, payload, mimeType, Charset.defaultCharset(), isHTTPS, unamePword64);
    }
    
        /**
 * 
 *  
 * @param method
 * @param host
 * @param port
 * @param path
 * @param payload
 * @param mimeType mimetype as string e.g. "application/json" will be added to the content type of the http call
 * @param charSet set charSet to null to omit sending a char flag (e.g. for binary files)
 * @param isHTTPS
 * @param unamePword64
 * @return
 * @throws MalformedURLException
 * @throws ProtocolException
 * @throws IOException 
 */
    public HTTPResponse doHTTPRequest(String method, String host, int port, String path, String payload, String mimeType, Charset charSet,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, ProtocolException, IOException {
        
        if (mimeType==null || mimeType.length()==0){
            mimeType = MimeTypes.getMimeType(""); //get unknown mime type if mimetype not set
        }
        String contentType = mimeType;
        if (charSet!=null){
            contentType += HttpConstants.SUB_FIELD_SEPARATOR+HttpConstants.CHARSET_CONTENT_TYPE_TAG+charSet.name(); //text/html; charset=utf-8
        }
        
         return doHTTPRequest(method, host, port, path, payload.getBytes(charSet) ,contentType, isHTTPS, unamePword64);
    
}
    
/**
 * 
 *  
 * @param method
 * @param host
 * @param port
 * @param path
 * @param payload
 * @param contentType content type  e.g. "application/json"
 * @param isHTTPS
 * @param unamePword64
 * @return
 * @throws MalformedURLException
 * @throws ProtocolException
 * @throws IOException 
 */
    public HTTPResponse doHTTPRequest(String method, String host, int port, String path, byte[] payload, String contentType,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, ProtocolException, IOException {

        if (payload == null) { //make sure payload is initialized
            payload = new byte[0];
        }

        URL url = getURL(host, port, path, isHTTPS);

        

        HttpURLConnection connection;
        if (isHTTPS) {
            connection = (HttpsURLConnection) url.openConnection();
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }

        connection.setRequestMethod(method);
        connection.setRequestProperty("Host", host);
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Content-Length", String.valueOf(payload.length));

        if (isHTTPS) {
            connection.setRequestProperty("Authorization", "Basic " + unamePword64);
        }
        
        Logger.getLogger(HttpHelper.class.getName()).log(Level.INFO, "trying to connect:\n" + method + " " 
                + url + "\nhttps:" + isHTTPS
                +"\nContentType:" + contentType
                +"\nContent-Length:" + String.valueOf(payload.length)                
                );
        

        connection.setDoInput(true);
        if (payload.length > 0) {
            // open up the output stream of the connection
            connection.setDoOutput(true);
            FilterOutputStream output = new FilterOutputStream(connection.getOutputStream());

            // write out the data
            output.write(payload);
            output.close();
        }

        HTTPResponse response = new HTTPResponse(method + " " + url.toString(), payload, Charset.defaultCharset()); //TODO forward charset ot this method

        response.code = connection.getResponseCode();
        response.message = connection.getResponseMessage();

        Logger.getLogger(HttpHelper.class.getName()).log(Level.FINE, "received response: "
                + response.message + " with code: " + response.code);

        if (response.code != 500) {

            // get ready to read the response from the cgi script
            DataInputStream input = new DataInputStream(connection.getInputStream());


            // read in each character until end-of-stream is detected
            for (int c = input.read(); c != -1; c = input.read()) {
                response.reply += (char) c + "";

            }
            input.close();
        }
        return response;
    }

    public HTTPResponse postMultiPartContainer(String host, int port, String path, MultipartContainer mpc,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, IOException {

        String method = HttpConstants.HTTP_COMMAND_POST;

        if (mpc == null) { //make sure multipartContainer is initialized
            return null;
        }

        URL url = getURL(host, port, path, isHTTPS);

        Logger.getLogger(HttpHelper.class.getName()).log(Level.FINE, "trying to connect " + method + " to " + url + " https:" + isHTTPS);

        HttpURLConnection connection;
        if (isHTTPS) {
            connection = (HttpsURLConnection) url.openConnection();
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        
        long length = mpc.getContentLength();
        Logger.getLogger(HttpHelper.class.getName()).log(Level.FINE, "contentLength: " + length);

        connection.setRequestMethod(method);
        connection.setRequestProperty("Host", host);
        connection.setRequestProperty("Content-Type", mpc.getContentType()); //content-type of multipart message must not have a charset field
        connection.setRequestProperty("Content-Length", ""+length);

        if (isHTTPS) {
            connection.setRequestProperty("Authorization", "Basic " + unamePword64);
        }


        connection.setDoInput(true);
        if (length > 0) {
            // open up the output stream of the connection
            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();

            // write out the data
            mpc.write(output);   
            output.close();
        }

        HTTPResponse response = new HTTPResponse(method + " " + url.toString(), "[multipart content]".getBytes(),
                HttpConstants.DEFAULT_CHARSET);


        response.code = connection.getResponseCode();
        response.message = connection.getResponseMessage();

        Logger.getLogger(HttpHelper.class.getName()).log(Level.FINE, "received response: "
                + response.message + " with code: " + response.code);

        if (response.code != 500) {

            // get ready to read the response from the cgi script
            DataInputStream input = new DataInputStream(connection.getInputStream());


            // read in each character until end-of-stream is detected
            for (int c = input.read(); c != -1; c = input.read()) {
                response.reply += (char) c + "";

            }
            input.close();
        }
        return response;

    }
}
