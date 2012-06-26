/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.multipart;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.ByteBuilder;
import sit.web.HttpConstants;

/**
 *
 * @author simon
 */
public class MultipartParser {

    public static MultipartContainer parse(String boundaryStr, Charset charSet, byte[] payload) {

        MultipartContainer result = new MultipartContainer();

        byte[] boundary = boundaryStr.getBytes(charSet);
        ByteBuilder content = new ByteBuilder(payload);


        int oldIndex = 0;

        int index;
        while (-1 != (index = content.indexOf(oldIndex, boundary))) {

            result.addPart(parsePart(charSet, content.subSequence(oldIndex + boundary.length, index)));
            oldIndex = index + boundary.length;
        }


        return result;
    }

    private static String extractValueIfFits(String field, String tag) {
        if (field.toLowerCase().startsWith(tag.toLowerCase())) {
            return field.substring(tag.length(), field.indexOf("\"", tag.length()));
        }
        return null;
    }

    private static MultipartEntry parsePart(Charset charSet, byte[] subSequence) {
        ByteBuilder content = new ByteBuilder(subSequence);
        String name = null;
        String mimeType = null;
        String fileName = null;
        TYPES type = TYPES.UNKNOWN;

        int endOfHeader = content.indexOf(HttpConstants.CRLFCRLF_BYTE);
        if (endOfHeader == -1) { //header not found or last entry ...
            Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "header not found in " + new String(subSequence, charSet));
            return null;
        }

        String header = new String(content.subSequence(0, endOfHeader), charSet);

        for (String headerLine : header.split(HttpConstants.CRLF)) {
            if (headerLine.toLowerCase().startsWith(HttpConstants.CONTENT_DISPOSITION_TAG.toLowerCase())) {
                String disposition = headerLine.substring(HttpConstants.CONTENT_DISPOSITION_TAG.length(), headerLine.length());
                for (String dispoField : disposition.split(";")) {
                    String myValue = extractValueIfFits(dispoField, HttpConstants.NAME_DISPOSITION_TAG); //TODO ugly code !!!
                    if (myValue!=null){
                        name = myValue;
                    }
                    myValue = extractValueIfFits(dispoField, HttpConstants.FILENAME_DISPOSITION_TAG); //TODO ugly code !!!
                    if (myValue!=null){
                        fileName = myValue;
                    }
                }
            }else if (headerLine.toLowerCase().startsWith(HttpConstants.CONTENT_TRANSFER_ENCODING_TAG.toLowerCase())) {
                type = TYPES.BINARY;
            }else if (headerLine.toLowerCase().startsWith(HttpConstants.CONTENT_TYPE_TAG.toLowerCase())) {
                String contentType = headerLine.substring(HttpConstants.CONTENT_TYPE_TAG.length(), headerLine.length());
                String [] contentTypeFields = contentType.split(";");
                    
                for (int i=0; i< contentTypeFields.length; i++){
                    if (i==0){ //first type is the mime-type
                        mimeType = contentTypeFields[i].trim();
                        type = TYPES.MIME;
                        if (mimeType.equalsIgnoreCase(HttpConstants.MIME_APPLICATION_OCTETSTREAM)){
                            type = TYPES.BINARY;
                        }
                    }else{
                        if (contentTypeFields[i].startsWith(HttpConstants.CHARSET_CONTENT_TYPE_TAG)){
                             
                            String myValue = extractValueIfFits(contentTypeFields[i], HttpConstants.CHARSET_CONTENT_TYPE_TAG); //TODO ugly code !!!
                            if (myValue!=null){
                                try{
                                    charSet = Charset.forName(myValue);
                                }catch (IllegalCharsetNameException ex){
                                    Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "charset :"+myValue+" illegal! Using "+charSet.name()+" instead!");
                                }catch (UnsupportedCharsetException ex){
                                    Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "charset :"+myValue+" not supported! Using "+charSet.name()+" instead!");                                 
                                }
                            }
                        }
                    }
                }
                
                 
            }
            
        }
        

        MultipartEntry result = null;
        if (type.equals(TYPES.BINARY)){
            result = new MPFileEntry(type, mimeType, name, fileName, content.subSequence(endOfHeader+HttpConstants.CRLFCRLF_BYTE.length));
        }else{
            result = new MPTextEntry(type, mimeType, name, new String(content.subSequence(endOfHeader+HttpConstants.CRLFCRLF_BYTE.length), charSet));
        }
        return result;
                


    }
}
