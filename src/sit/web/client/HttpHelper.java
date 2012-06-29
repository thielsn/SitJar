/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.security.KeyStore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
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
        }

        if (!charSet.equals(Charset.defaultCharset())) {
            Logger.getLogger(HttpHelper.class.getName()).log(Level.WARNING, "unexpected charset: " + charSet + " should be " + Charset.defaultCharset());
        }
        try {
            return doHTTPRequest(method, host, port, path, payload.getBytes(), contentType, isHTTPS, unamePword64);
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
     */
    public HTTPResponse doHttpURLConnectionRequest(String method, String host, int port, String path, byte[] payload, String contentType,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, ProtocolException, IOException, URISyntaxException {

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
