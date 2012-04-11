package sit.web.client;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.json.JSONObject;
import sit.json.JSONParseException;
import sit.json.JSONParser;
import sit.json.JSONPathAccessException;

/**
 *
 * @author simon
 */
public class JSONResponse extends HTTPResponse {
    public JSONObject replyObject= null;
    public Vector<JSONObject> replyObjects = null;

    private JSONParser parser = new JSONParser();



    public JSONResponse(String call, String payload) {
        super(call, payload);
    }



    public void parseJSONResponse() {
        try {
            replyObject = parser.parseJSON(reply);
        
        } catch (JSONParseException ex) {

            Logger.getLogger(JSONResponse.class.getName()).log(Level.FINE, null, ex);
            isErrorMessage = true;
            return;
        }

        try {
            JSONObject status = replyObject.getChild(statusPath);
            if (!status.getValue().equalsIgnoreCase("OK")){
                isErrorMessage = true;
                return;
            }
        } catch (JSONPathAccessException ex) {
            
            Logger.getLogger(JSONResponse.class.getName()).log(Level.FINE, null, ex);
            isErrorMessage = true;
            return;
        }


         try {
            replyObject.getChild(dataPath);
        } catch (JSONPathAccessException ex) {
            noDataFound = true;
            return;
        }

        try {
            replyObject.getChild(entryPath);
        } catch (JSONPathAccessException ex) {
            noDataEntryFound = true;
            return;
        }
      
        try {
            replyObjects = replyObject.getChild(entryPath).getItems();
        } catch (JSONPathAccessException ex) {
            Logger.getLogger(JSONResponse.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String getErrorReasonString(){
        if (callFailed){
            return "failure when calling endpoint";
        }//else
        if (isErrorMessage) {
           return "error message or undefined status";
        }//else
        if (noDataFound) {
            return "data element missing";
        }
        if (noDataEntryFound) {
            return"data.entry element missing";
        }
        if (replyObjects.isEmpty()) {
            return"data.entry element is empty";
        }
        return "";

    }

     public String toShortString() {
        if (!hasError()) {
            return call + " - OK - received " + replyObjects.size() + " item(s)";
        } else {
            return call + " - ERROR - " + getErrorReasonString();
        }
    }

   
}
