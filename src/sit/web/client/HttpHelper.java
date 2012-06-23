/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author simon
 */
public class HttpHelper {

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

    public HTTPResponse doHTTPRequest(String method, String host, int port, String path, String payload,
            boolean isHTTPS, String unamePword64) throws MalformedURLException, ProtocolException, IOException {

        if (payload == null) { //make sure payload is initialized
            payload = "";
        }

        URL url = getURL(host, port, path, isHTTPS);

        Logger.getLogger(HttpHelper.class.getName()).log(Level.FINE, "trying to connect " + method + " to " + url + " https:" + isHTTPS);

        HttpURLConnection connection;
        if (isHTTPS) {
            connection = (HttpsURLConnection) url.openConnection();
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }


        connection.setRequestMethod(method);
        connection.setRequestProperty("Host", host);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-length", String.valueOf(payload.
                length()));

        if (isHTTPS) {
            connection.setRequestProperty("Authorization", "Basic " + unamePword64);
        }


        connection.setDoInput(true);
        if (payload.length() > 0) {
            // open up the output stream of the connection
            connection.setDoOutput(true);
            DataOutputStream output = new DataOutputStream(connection.
                    getOutputStream());

            // write out the data
            output.writeBytes(payload);
            output.close();
        }

        HTTPResponse response = new HTTPResponse(method + " " + url.toString(), payload);


        response.code = connection.getResponseCode();
        response.message = connection.getResponseMessage();

        Logger.getLogger(HttpHelper.class.getName()).log(Level.FINE, "received response: "
                + response.message + " with code: " + response.code);

        if (response.code != 500) {

            // get ready to read the response from the cgi script
            DataInputStream input = new DataInputStream(connection.
                    getInputStream());


            // read in each character until end-of-stream is detected
            for (int c = input.read(); c != -1; c = input.read()) {
                response.reply += (char) c + "";

            }
            input.close();
        }
        return response;
    }
}
