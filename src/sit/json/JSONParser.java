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

/*
 *  Description of JSONParser
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @date 26.03.2012
 */
package sit.json;

/**
 *
 * @author simon
 */
public class JSONParser {

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
        result.value = JSONTextHelper.decodeText(text.substring(start,curC));
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


   
}
