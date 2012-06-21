package sit.web.client;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
public class ApacheRestAPI {
	
	private DefaultHttpClient filesHttpClient;
	private DefaultHttpClient contactsHttpClient;
	
	private SSLSocketFactory sslFactory;
	
	private static final String TAG = "PersonalServerAPI";
	
	
	private final String host;
	private int portNumber;
	private final String protocol;
	
	// keep these hardcoded?	
	private final static String sendContactsUrl    = "/dime-communications/api/dime/rest/5b223cde-07e5-4987-b531-231876fa7601/profile/jsonld";
	
	private final static String sendFilesUrl       = "/dime-communications/api/dime/rest/5b223cde-07e5-4987-b531-231876fa7601/resource/crawler";
	
	public ApacheRestAPI(String protocol,String host, String port, String username, String password) {
		this.host = host;
		this.protocol = protocol;
		HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        
        portNumber = 80;
        
        if (port != null && port.trim().length() > 0){
        	try {
        		portNumber = Integer.valueOf(port);
        	}catch (Exception e){ 
        		portNumber = 80;
        	}
        }
        
        AuthScope authScope = new AuthScope(host, portNumber);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        
        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        
        
        filesHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params,  mgr.getSchemeRegistry()), params);
        filesHttpClient.getCredentialsProvider().setCredentials(authScope, credentials);
        
        contactsHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params,  mgr.getSchemeRegistry()), params);        
        contactsHttpClient.getCredentialsProvider().setCredentials(authScope, credentials);
        
	}

    private class CrawlUploadResponseHandler implements ResponseHandler {
    	
        @Override
        public Object handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {

            HttpEntity r_entity = response.getEntity();
            String responseString = EntityUtils.toString(r_entity);
            Log.d(TAG, responseString);

            return null;
        }
    }
    
    public void sendContactData(String serializedRDF){
    	HttpPost post = new HttpPost(protocol + host + ":" + portNumber + sendContactsUrl);
    	
    	try {
			StringEntity entity = new StringEntity(serializedRDF, "UTF-8");
			entity.setContentType("application/x-turtle");
			post.setEntity(entity);
			try {
				contactsHttpClient.execute(post, new CrawlUploadResponseHandler());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void sendCrawlData(CrawlData data, File file){
    	try{    		
			final HttpPost post = new HttpPost(protocol + host + ":" + portNumber + sendFilesUrl);
			
	        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
	        multipartEntity.addPart("file", new FileBody(file));
	        multipartEntity.addPart("uri", new StringBody(file.toURI().toString()));
	        multipartEntity.addPart("hash", new StringBody(data.getHash()));
	        multipartEntity.addPart("metadata", new StringBody(data.getMetadata().getContent()));
	        multipartEntity.addPart("syntax", new StringBody(data.getMetadata().getMimeType()));
	        post.setEntity(multipartEntity);
	        
	        // Execute post action in new thread; network activity is not allowed on main thread
	        	Thread thread = new Thread(){
	        	    @Override
	        	    public void run() {
	        	        try {
							filesHttpClient.execute(post, new CrawlUploadResponseHandler());
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
	        	    }
	        	};

	        	thread.start();
	    
		} catch (Exception e) {
			Log.e(TAG, "Error uploading crawled file: " + e.getMessage());
		}
    }
    
    public void doShutdown(){
    	if (filesHttpClient != null){
    		try {
    			filesHttpClient.getConnectionManager().shutdown();
    		}catch(Exception e){
    			filesHttpClient = null;
    		}
    	}
    	if (contactsHttpClient != null){
    		try {
    			contactsHttpClient.getConnectionManager().shutdown();
    		}catch(Exception e){
    			contactsHttpClient = null;
    		}
    	}
    }
    
}
