
package sit.json;

/**
 *
 * @author simon
 */
public class JSONParser {

    private final static String TESTJSON = "{\n     \"firstName\": \"John\",\n     \"lastName\" : \"Smith\",\n     \"age\"      : 25,\n     \"address\"  :\n     {\n         \"streetAddress\": \"21 2nd Street\",\n         \"city\"         : \"New York\",\n         \"state\"        : \"NY\",\n         \"postalCode\"   : \"10021\"\n     },\n     \"phoneNumber\":\n     [\n         {\n           \"type\"  : \"home\",\n           \"number\": \"212 555-1234\"\n         },\n         {\n           \"type\"  : \"fax\",\n           \"number\": \"646 555-4567\"\n         }\n     ]\n }";
    private final static String REST_CALL_RESPONSE = "{\"response\":{\"meta\":{},\"data\":{\"startIndex\":0,\"itemsPerPage\":20,\"totalResults\":20,\"entry\":[{\"guid\":\"p_1\",\"name\":\"Achille\",\"imageUrl\":\"http://lorempixel.com/200/200/people/1\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_2\",\"name\":\"Adelheid\",\"imageUrl\":\"http://lorempixel.com/200/200/people/2\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_3\",\"name\":\"Xaverius\",\"imageUrl\":\"http://lorempixel.com/200/200/people/3\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_4\",\"name\":\"Yvette\",\"imageUrl\":\"http://lorempixel.com/200/200/people/4\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_5\",\"name\":\"Alkwin\",\"imageUrl\":\"http://lorempixel.com/200/200/people/5\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_1\",\"name\":\"Quintin\",\"imageUrl\":\"http://lorempixel.com/200/200/people/6\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_2\",\"name\":\"Juana\",\"imageUrl\":\"http://lorempixel.com/200/200/people/7\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_3\",\"name\":\"Seymour\",\"imageUrl\":\"http://lorempixel.com/200/200/people/8\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_4\",\"name\":\"Umberto\",\"imageUrl\":\"http://lorempixel.com/200/200/people/9\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_1_5\",\"name\":\"Sinja\",\"imageUrl\":\"http://lorempixel.com/200/200/people/10\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_1\",\"name\":\"Reinhard\",\"imageUrl\":\"http://lorempixel.com/200/200/people/1\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_2\",\"name\":\"Arne\",\"imageUrl\":\"http://lorempixel.com/200/200/people/2\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_3\",\"name\":\"Richard\",\"imageUrl\":\"http://lorempixel.com/200/200/people/3\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_4\",\"name\":\"Pascal\",\"imageUrl\":\"http://lorempixel.com/200/200/people/4\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_2_5\",\"name\":\"Kim\",\"imageUrl\":\"http://lorempixel.com/200/200/people/5\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_1\",\"name\":\"Baptiste\",\"imageUrl\":\"http://lorempixel.com/200/200/people/6\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_2\",\"name\":\"Boris\",\"imageUrl\":\"http://lorempixel.com/200/200/people/7\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_3\",\"name\":\"Indira\",\"imageUrl\":\"http://lorempixel.com/200/200/people/8\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_4\",\"name\":\"Trude\",\"imageUrl\":\"http://lorempixel.com/200/200/people/9\",\"type\":\"person\",\"items\":[]},{\"guid\":\"p_g_3_5\",\"name\":\"Vanessa\",\"imageUrl\":\"http://lorempixel.com/200/200/people/10\",\"type\":\"person\",\"items\":[]}]}}}";
    private final static String REST_CALL_RESPONSE2 = "{\"response\":{\"meta\":{},\"data\":{\"startIndex\":0,\"itemsPerPage\":3,\"totalResults\":3,\"entry\":[{\"guid\":\"g_1\",\"name\":\"Business\",\"imageUrl\":\"http://localhost:8080/images/group.png\",\"type\":\"group\",\"items\":[\"p_g_1_1\",\"p_g_1_2\",\"p_g_1_3\",\"p_g_1_4\",\"p_g_1_5\"]},{\"guid\":\"g_2\",\"name\":\"Friends\",\"imageUrl\":\"http://localhost:8080/images/group.png\",\"type\":\"group\",\"items\":[\"p_g_2_1\",\"p_g_2_2\",\"p_g_2_3\",\"p_g_2_4\",\"p_g_2_5\"]},{\"guid\":\"g_3\",\"name\":\"Family\",\"imageUrl\":\"http://localhost:8080/images/group.png\",\"type\":\"group\",\"items\":[\"p_g_3_1\",\"p_g_3_2\",\"p_g_3_3\",\"p_g_3_4\",\"p_g_3_5\"]}]}}}";
    private final static String REST_CALL_RESPONSE3 = "{\"response\":{\"meta\":{\"v\":\"0.1\",\"status\":\"OK\",\"code\":200,\"timeRef\":1320237974811},\"data\":{\"startIndex\":0,\"itemsPerPage\":3,\"totalResults\":3,\"entry\":[{\"guid\":\"urn:juan:juanPerson01/privacyPreference01\",\"type\":\"databox\",\"rdfs:label\":[\"DATABOX\",\"digital enterprise materials\"],\"name\":\"digital enterprise materials\",\"ppo:hasAccessSpace\":\"urn:juan:juanPerson01/privacyPreference01/accessSpace01\"},{\"guid\":\"urn:juan:juanPerson01/privacyPreference02\",\"type\":\"databox\",\"rdfs:label\":[\"DATABOX\",\"social media b2b\"],\"name\":\"social media b2b\",\"ppo:hasAccessSpace\":\"urn:juan:juanPerson01/privacyPreference02/accessSpace01\"},{\"guid\":\"urn:juan:juanPerson01/privacyPreference03\",\"type\":\"databox\",\"rdfs:label\":[\"DATABOX\",\"digitalHomeTechDocs\"],\"name\":\"digitalHomeTechDocs\",\"ppo:hasAccessSpace\":\"urn:juan:juanPerson01/privacyPreference03/accessSpace01\"}]}}}";
    private boolean isWhiteSpace(char chr) {
        return (chr == ' ' || chr == '\t' || chr == '\n' | chr == '\f' | chr == '\r');
    }

    private int ignoreWhiteSpace(String text, int curC) {
        while (curC < text.length() && isWhiteSpace(text.charAt(curC))) {
            curC++;
        }
        return curC;
    }

    private boolean testForChr(String text, int index, char chr){
        if (index>=text.length()){
            return false;
        }
        return text.charAt(index)==chr;
    }

    private int jumpOverNextChr(String text, int curC, char chr){
        curC = ignoreWhiteSpace(text, curC);
        if (testForChr(text, curC, chr)){
            curC++;
        }
        curC = ignoreWhiteSpace(text, curC);
        return curC;
    }

    private ParseResult getNextTextValue(String text, int curC, char endDelim) throws JSONParseException {

        ParseResult result = new ParseResult();

        if (testForChr(text,curC,'"')){          
            result.hasQuotes = true;
            curC++;
        }
        boolean escapeFlag=false;
        int start = curC;
        
        while (curC<text.length() 
                && (!result.hasQuotes || (!testForChr(text,curC,'"')) || escapeFlag)
                && (result.hasQuotes || (!testForChr(text,curC,endDelim)))
                && (result.hasQuotes || (!testForChr(text,curC,'}')) || escapeFlag)
                && (result.hasQuotes || (!testForChr(text,curC,']')) || escapeFlag)
                ){ //missing additonal delim for end of object in case no quotes are set!!!!

            escapeFlag = (testForChr(text,curC,'\\'));
            curC++;
        }
        result.value = text.substring(start,curC);
        if (result.hasQuotes){
            curC++;
            curC = ignoreWhiteSpace(text, curC);
        }
       
        result.endPosition = curC;
        return result;
    }



    public ParseResult getNextObject(String key, String text, int curC) throws JSONParseException {

        
        curC = ignoreWhiteSpace(text, curC);
        if (!testForChr(text,curC,'{')){
            if (curC>=text.length()){
                throw new JSONParseException("curC>text.length() - reached end of text when looking for }\nkey:"+key+"\ntext:\n"+text);
            }else{
                throw new JSONParseException("Found unexpected char "+text.charAt(curC)+" at begining of object at pos: "+curC+" in (starting from "+curC+"-2):\n"+text.substring((0<curC-2)?curC-2:0));
            }
        }//else
        curC++;
        ParseResult result = new ParseResult();
        result.object = new JSONObject(key);

        boolean hasNextKey = true;
        while (curC<text.length() && (!testForChr(text,curC,'}')) && hasNextKey){
            ParseResult myKey = getNextKey(text, curC);
            curC = myKey.endPosition;

            //proceed to value in the text
            curC = jumpOverNextChr(text, curC, ':');

            if (myKey.value!=null){
                ParseResult myValue = getNextValue(myKey.value, text, curC);                

                if (myValue.object!=null){
                    result.object.addChild(myValue.object);
                    curC = myValue.endPosition;
                }else{
                    throw new JSONParseException("unable to parse right side at pos: "+curC+" in:\n"+text);
                }
            }else{
                hasNextKey=false;
            }            
            //proceed to next key in the text if there is one
            curC = jumpOverNextChr(text, curC, ',');
        }
        curC = ignoreWhiteSpace(text, curC);
        if (!testForChr(text,curC,'}')){
            if (curC>=text.length()){
                throw new JSONParseException("curC>text.length() - reached end of text when looking for }\nkey:"+key+"\ntext:\n"+text);
            }else{
                throw new JSONParseException("Found unexpected char "+text.charAt(curC)+" at end object at pos: "+curC+" in:\n"+text);
            }
        }
        result.endPosition = curC;
        return result;
    }

    private ParseResult getNextValue(String key, String text, int curC) throws JSONParseException {
        ParseResult result = new ParseResult();

        curC = ignoreWhiteSpace(text, curC);
        if (testForChr(text,curC,'[')) { //we have a collection

            result.object = new JSONObject(key);
            result.object.setType(JSONObject.JSON_TYPE_COLLECTION);
            curC++;
            curC = ignoreWhiteSpace(text, curC);
            boolean objectFound = true;
            int counter = 0;


            while (curC<text.length() && (!testForChr(text,curC,']')) && objectFound){
                ParseResult parseResult = getNextValue(counter+"",text, curC); //we set the index of the object/value in a collection as its key
                counter++;
                if (parseResult.object!=null){
                    result.object.addItem(parseResult.object);
                }else{
                    objectFound = false;
                }
                curC = parseResult.endPosition;
                
                //proceed to next object in the text (if there is one)
                curC = jumpOverNextChr(text, curC, ',');
            }
            curC = jumpOverNextChr(text, curC, ']');

        } else if (testForChr(text,curC,'{')) { //we have an object

                ParseResult parseResult = getNextObject(key, text, curC);
                if (parseResult.object!=null){
                    result.object = parseResult.object;
                }else{
                    throw new JSONParseException("Found invalid object at pos: "+curC+" in:\n"+text);
                }
                curC = parseResult.endPosition;
                curC = jumpOverNextChr(text, curC, '}');
                curC = jumpOverNextChr(text, curC, ',');

        } else { //we have a leave
            ParseResult parseResult = getNextTextValue(text, curC, ',');
            result.object = new JSONObject(key);
            result.object.setValue(parseResult.value, parseResult.hasQuotes);
            curC = parseResult.endPosition;

        }
        result.endPosition = curC;
        return result;
    }

    private ParseResult getNextKey(String text, int curC) throws JSONParseException {
        curC = ignoreWhiteSpace(text, curC);
        ParseResult result = getNextTextValue(text, curC, ':');
        return result;
    }

    public JSONObject parseJSON(String text) throws JSONParseException{
        
            return getNextObject("root", text, 0).object;
       
    }


     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        System.out.println("json:\n"+TESTJSON);
        System.out.println("-------------------------------");
        try {
            JSONObject root = parser.getNextObject("root", TESTJSON, 0).object;
            System.out.println("-----------------------------------------");
            System.out.println(root.toJson());
            System.out.println("-----------------------------------------");
            root = parser.getNextObject("root", REST_CALL_RESPONSE2, 0).object;
            System.out.println("-----------------------------------------");
            System.out.println(root.toJson());
            System.out.println("-----------------------------------------");
            System.out.println(root.toString());
            System.out.println("-----------------------------------------");
            
            String[] guidPath = {"response","data","entry","0","guid"};
            JSONObject guid = root.getChild(guidPath);
            System.out.println("guid="+guid.getValue());
            guid.setValue("hello world",true);
            guid = root.getChild(guidPath);
            System.out.println("guid="+guid.getValue());

            root = parser.getNextObject("root", REST_CALL_RESPONSE3, 0).object;
            System.out.println("-----------------------------------------");
            System.out.println(root.toJson());
            System.out.println("-----------------------------------------");
            System.out.println(root.toString());
            System.out.println("-----------------------------------------");
            
        } catch (JSONParseException ex) {
                     
           //Logger.getLogger(JSONParser.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            for (StackTraceElement line: ex.getStackTrace()){
                System.out.println(line.toString());
            }
            
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            for (StackTraceElement line: ex.getStackTrace()){
                System.out.println(line.toString());
            }
        }


    }
}
