/*
* Copyright 2013 Simon Thiel
*
* This file is part of SitJar.
*
* SitJar is free software: you can redistribute it and/or modify
* it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SitJar is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.
*/

package sit.web.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import sit.sstl.ByteBuilder;
import sit.tools.Base64;
import sit.web.HttpConstants;
import sit.web.MimeTypes;
import sit.web.multipart.MultipartContainer;

public class HttpHelper {

    private final static boolean USE_APACHE = true;

    public static String getBase64UserNamePwdToken(String username, String password) {
        return Base64.encodeBytes((username + ":" + password).getBytes());
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

    public static URI getURI(String host, int port, String path, boolean isHTTPS)
            throws MalformedURLException, URISyntaxException {

        String myUrl = isHTTPS ? "https" : "http";
        myUrl += "://" + host + ":" + port + path;

        return new URI(myUrl);
    }

    public static URL getURL(String host, int port, String path, boolean isHTTPS)
            throws MalformedURLException {

        String myUrl = isHTTPS ? "https" : "http";
        myUrl += "://" + host + ":" + port + path;

        return new URL(myUrl);
    }

   
    public HttpHelper() {
        HTTPTrustHelper.initAllTrustingManager("TLS"); //or "SSL"
    }

    public HttpHelper(String sslContext) {
        HTTPTrustHelper.initAllTrustingManager(sslContext);
    }

    public HTTPResponse postMultiPartContainer(String host, int port, String path, MultipartContainer mpc,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, IOException, ProtocolException {

        if (mpc == null) { //make sure multipartContainer is initialized
            return null;
        }

        String contentType = mpc.getContentType();
        ByteBuilder bb = new ByteBuilder();
        OutputStream os = bb.getOutputStream();
        mpc.write(os);
        os.close();

        Logger.getLogger(HttpHelper.class.getName()).log(Level.INFO, "mpc length " + mpc.getContentLength() + " bb.size():" + bb.size());

        byte[] payload = bb.toByteArray();
        try {
            return doHTTPRequest(HttpConstants.HTTP_COMMAND_POST, host, port, path, payload, contentType, isHTTPS, unamePword64);
        } catch (URISyntaxException ex) {
            Logger.getLogger(HttpHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     * convenient call, sets charSet automatically to
     * <code>Charset.defaultCharset()</code>
     *
     * @param method
     * @param host
     * @param port
     * @param path
     * @param payload
     * @param mimeType mimetype as string e.g. "application/json" will be added
     * to the content type of the http call
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
     * @param mimeType mimetype as string e.g. "application/json" will be added
     * to the content type of the http call
     * @param charSet set charSet to null to omit sending a char flag (e.g. for
     * binary files)
     * @param isHTTPS
     * @param unamePword64
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    public HTTPResponse doHTTPRequest(String method, String host, int port, String path, String payload, String mimeType, Charset charSet,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, ProtocolException, IOException {

        if (mimeType == null || mimeType.length() == 0) {
            mimeType = MimeTypes.getMimeType(""); //get unknown mime type if mimetype not set
        }
        String contentType = mimeType;
        if (charSet != null) {
            contentType += HttpConstants.SUB_FIELD_SEPARATOR + HttpConstants.CHARSET_CONTENT_TYPE_TAG + charSet.name(); //text/html; charset=utf-8
            //##CHARSET_MARKER##            
        }else{
            charSet = Charset.defaultCharset();
        }
        
        try {
            return doHTTPRequest(method, host, port, path, payload.getBytes(charSet), contentType, isHTTPS, unamePword64);
        } catch (URISyntaxException ex) {
            
            throw new MalformedURLException(ex.getMessage());
        }

    }

    /**
     *
     *
     * @param method
     * @param host
     * @param port
     * @param path
     * @param payload
     * @param contentType content type e.g. "application/json"
     * @param isHTTPS
     * @param unamePword64
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    public HTTPResponse doHTTPRequest(String method, String host, int port, String path, byte[] payload, String contentType,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, ProtocolException, IOException, URISyntaxException {

       if (USE_APACHE){
           return doApacheHTTPRequest(method, host, port, path, payload, contentType, isHTTPS, unamePword64);
       }//else
       
       return doHttpURLConnectionRequest(method, host, port, path, payload, contentType, isHTTPS, unamePword64);
    }
    
      /**
     *
     *
     * @param method
     * @param host
     * @param port
     * @param path
     * @param payload
     * @param contentType content type e.g. "application/json"
     * @param isHTTPS
     * @param unamePword64
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     * @throws URISyntaxException  
     */
    public HTTPResponse doApacheHTTPRequest(String method, String host, int port, String path, byte[] payload, String contentType,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, ProtocolException, IOException, URISyntaxException {

        if (payload == null) { //make sure payload is initialized
            payload = new byte[0];
        }

        URI url = getURI(host, port, path, isHTTPS);

        HttpClient httpclient;
        if (isHTTPS) {
            httpclient = HTTPTrustHelper.getNewHttpClient(Charset.defaultCharset(), port);
        } else {
            httpclient = new DefaultHttpClient();
        }

        HTTPResponse result = null;
        HttpResponse response = null;

        if (method.equalsIgnoreCase(HttpConstants.HTTP_COMMAND_GET)) {
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", "Basic " + unamePword64);
            response = httpclient.execute(request);
        } else if (method.equalsIgnoreCase(HttpConstants.HTTP_COMMAND_POST)) {
            HttpPost request = new HttpPost(url);
            request.setHeader("Authorization", "Basic " + unamePword64);
            InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(payload), payload.length);
            request.setEntity(reqEntity);
            reqEntity.setContentType(contentType);
            response = httpclient.execute(request);
        } else if (method.equalsIgnoreCase(HttpConstants.HTTP_COMMAND_PUT)) {
            HttpPut request = new HttpPut(url);
            request.setHeader("Authorization", "Basic " + unamePword64);
            InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(payload), payload.length);
            request.setEntity(reqEntity);
            reqEntity.setContentType(contentType);
            response = httpclient.execute(request);
        } else if (method.equalsIgnoreCase(HttpConstants.HTTP_COMMAND_DELETE)) {
            HttpDelete request = new HttpDelete(url);
            request.setHeader("Authorization", "Basic " + unamePword64);
            response = httpclient.execute(request);
        } else {
            throw new RuntimeException("HTTP method not supported! Method:" + method);
        }


        result = new HTTPResponse(path, payload, Charset.defaultCharset());
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            result.reply = EntityUtils.toString(responseEntity);
        }
        result.code = response.getStatusLine().getStatusCode();
        result.message = response.getStatusLine().getReasonPhrase();



        httpclient.getConnectionManager().shutdown();
        return result;
    }
   /**
     *
     *
     * @param method
     * @param host
     * @param port
     * @param path
     * @param payload
     * @param contentType content type e.g. "application/json"
     * @param isHTTPS
     * @param unamePword64
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     * @throws URISyntaxException  
     */
    public HTTPResponse doHttpURLConnectionRequest(String method, String host, int port, String path, byte[] payload, String contentType,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, ProtocolException, IOException, URISyntaxException {

        
        URL url = getURL(host, port, path, isHTTPS);

        return doHttpUrlConnectionRequest(url, method, contentType, payload, unamePword64);
    }

    public HTTPResponse doHttpUrlConnectionRequest(URL url, String method, String contentType, byte [] payload, String unamePword64) throws MalformedURLException, ProtocolException, IOException, URISyntaxException{
        
        if (payload == null) { //make sure payload is initialized
            payload = new byte[0];
        }

        boolean isHTTPS = url.getProtocol().equalsIgnoreCase("https");
        HttpURLConnection connection;
        
        if (isHTTPS) {
            connection = (HttpsURLConnection) url.openConnection();
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }

        connection.setRequestMethod(method);
        connection.setRequestProperty("Host", url.getHost());
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Content-Length", String.valueOf(payload.length));

        if (isHTTPS) {
            connection.setRequestProperty("Authorization", "Basic " + unamePword64);
        }
        
        Logger.getLogger(HttpHelper.class.getName()).log(Level.FINER, "trying to connect:\n" + method + " " 
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
            byte[] buffer =new byte[20480];

            ByteBuilder bytes = new ByteBuilder();

            
            // get ready to read the response from the cgi script
            DataInputStream input = new DataInputStream(connection.getInputStream());
            boolean done = false;
            while(!done){
                int readBytes = input.read(buffer);
                done=(readBytes==-1);

                if(!done){
                    bytes.append(buffer,readBytes);
                }
            }
            input.close();
            response.reply=bytes.toString(Charset.defaultCharset());
        }
        return response;
    }           
    
}
