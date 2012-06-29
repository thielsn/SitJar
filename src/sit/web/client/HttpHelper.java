/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import sit.io.IOString;
import sit.tools.Base64;
import sit.web.HttpConstants;
import sit.web.MimeTypes;
import sit.web.multipart.MultipartContainer;

public class HttpHelper {

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
        HTTPUrlConnectionHelper.initAllTrustingManager("TLS"); //or "SSL"
    }

    public HttpHelper(String sslContext) {
        HTTPUrlConnectionHelper.initAllTrustingManager(sslContext);
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

            //throw new RuntimeException("other charsets than "+Charset.defaultCharset()+" not allowed in this implementation! charset:"+charSet);
        }
        try {
            return doHTTPRequest(method, host, port, path, payload.getBytes(), contentType, isHTTPS, unamePword64);
        } catch (URISyntaxException ex) {
            //Logger.getLogger(HttpHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw new MalformedURLException(ex.getMessage());
        }

    }

    /**
     * from
     * http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https
     *
     * @param charset
     * @param port
     * @return
     */
    private HttpClient getNewHttpClient(Charset charset, int port) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, charset.name());

            SchemeRegistry registry = new SchemeRegistry();
            //registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, port));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
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

        if (payload == null) { //make sure payload is initialized
            payload = new byte[0];
        }

        URI url = getURI(host, port, path, isHTTPS);
        
        HttpClient httpclient;
        if (isHTTPS){
            httpclient = getNewHttpClient(Charset.defaultCharset(), port);
        }else{
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

        }
        HttpEntity responseEntity = response.getEntity();

        if (responseEntity != null) {
            responseEntity.consumeContent();
            result = new HTTPResponse(path, payload, Charset.defaultCharset());
            result.reply = IOString.convertStreamToString(responseEntity.getContent());
            result.code = response.getStatusLine().getStatusCode();
            result.message = response.getStatusLine().getReasonPhrase();
        }


        httpclient.getConnectionManager().shutdown();
        return result;


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
        connection.setRequestProperty("Content-Length", "" + length);

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
