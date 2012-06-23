/*
 *  Description of HTTPUrlConnectionHelper
 * 
 *  @author Simon Thiel
 *  @version $Revision: $
 *  @date 21.06.2012
 */
package sit.web.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * HTTPUrlConnectionHelper
 *
 */
class HTTPUrlConnectionHelper {

    // Create a trust manager that does not validate certificate chains
    private static TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

    public static void initAllTrustingManager(String securityAlgorithm) {
        init(securityAlgorithm, null, trustAllCerts);
    }

    public static void init(String securityAlgorithm, KeyManager[] kms, TrustManager[] tms) {
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance(securityAlgorithm);
            sc.init(kms, tms, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception ex) {
            Logger.getLogger(HttpHelper.class.getName()).log(Level.SEVERE, ex.
                    getMessage(), ex);
        }

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String string, SSLSession ssls) {
                return true;
            }
        });
        HttpsURLConnection.setFollowRedirects(true);
    }
}
