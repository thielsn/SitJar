/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web.multipart;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.ByteBuilder;
import sit.web.HTTPParseHelper;
import sit.web.HttpConstants;
import sit.web.WebRequest;
import sit.web.WebRequest.ContentType;

/**
 *
 * @author simon
 */
public class MultipartParser {

    public static MultipartContainer parse(String boundaryStr, Charset charSet, byte[] payload) throws UnsupportedEncodingException {

        MultipartContainer result = new MultipartContainer();

        if (!charSet.equals(Charset.defaultCharset())) {
            Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "unexpected charset: "+charSet+ " should be "+ Charset.defaultCharset());
            //throw new RuntimeException("other charsets than " + Charset.defaultCharset() + " not allowed in this implementation! charset:" + charSet);
        }
        byte[] boundary = boundaryStr.getBytes();
        ByteBuilder content = new ByteBuilder(payload);


        int oldIndex = boundary.length + HttpConstants.CRLF_BYTE.length;

        int index;
        while (-1 != (index = content.indexOf(oldIndex, boundary))) {


        result.addPart(parsePart(charSet, content.subSequence(oldIndex, index-HttpConstants.CRLF_BYTE.length)));    
        oldIndex = index + boundary.length + HttpConstants.CRLF_BYTE.length;
        
        }


        return result;
    }

    private static MultipartEntry parsePart(Charset charSet, byte[] subSequence) {

        ByteBuilder content = new ByteBuilder(subSequence);
        String name = null;
        String fileName = null;
        TYPES type = TYPES.UNKNOWN;
        ContentType contentType = new WebRequest.ContentType();
        contentType.charSet = charSet;

        int endOfHeader = content.indexOf(HttpConstants.CRLFCRLF_BYTE);
        if (endOfHeader == -1) { //header not found or last entry ...
            Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "header not found in " + new String(subSequence, charSet));
            return null;
        }
        //TODO  re-use HTTPMessage parser for parsing the headers !!!
        if (!charSet.equals(Charset.defaultCharset())) {
            Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "unexpected charset: "+charSet+ " should be "+ Charset.defaultCharset());
            //throw new RuntimeException("other charsets than " + Charset.defaultCharset() + " not allowed in this implementation! charset:" + charSet);
        }
        String header = new String(content.subSequence(0, endOfHeader));

        HashMap<String, String> headerFields = HTTPParseHelper.parseAndFillFittingValues(
                new String[]{HttpConstants.CONTENT_DISPOSITION_TAG,
                    HttpConstants.CONTENT_TRANSFER_ENCODING_BINARY_TAG,
                    HttpConstants.CONTENT_TYPE_TAG}, header.split(HttpConstants.CRLF));

        if (headerFields.containsKey(HttpConstants.CONTENT_DISPOSITION_TAG)) {

            String disposition = headerFields.get(HttpConstants.CONTENT_DISPOSITION_TAG);

            HashMap<String, String> dispoFields = HTTPParseHelper.parseAndFillFittingValues(
                    new String[]{HttpConstants.NAME_DISPOSITION_TAG,
                        HttpConstants.FILENAME_DISPOSITION_TAG}, disposition.split(HttpConstants.SUB_FIELD_SEPARATOR));

            name = dispoFields.get(HttpConstants.NAME_DISPOSITION_TAG);
            fileName = dispoFields.get(HttpConstants.FILENAME_DISPOSITION_TAG);


        }
        if (headerFields.containsKey(HttpConstants.CONTENT_TRANSFER_ENCODING_BINARY_TAG)) {
            type = TYPES.BINARY;
        }
        if (headerFields.containsKey(HttpConstants.CONTENT_TYPE_TAG)) {

            String contentTypePayload = headerFields.get(HttpConstants.CONTENT_TYPE_TAG);

            contentType.parseContentType(contentTypePayload);
            if (contentType.mimeType.equalsIgnoreCase(HttpConstants.MIME_APPLICATION_OCTETSTREAM)) {
                type = TYPES.BINARY;
            } else if (!contentType.mimeType.equalsIgnoreCase(HttpConstants.DEFAULT_MIME_TYPE)) {
                type = TYPES.MIME;
            }
            charSet = contentType.charSet; //possibly been changed by content-type setting...

        }




        MultipartEntry result = null;
        if (type.equals(TYPES.BINARY)) {
            result = new MPFileEntry(type, contentType.mimeType, name, fileName, content.subSequence(endOfHeader + HttpConstants.CRLFCRLF_BYTE.length));
        } else {
            if (!charSet.equals(Charset.defaultCharset())) {
                Logger.getLogger(MultipartParser.class.getName()).log(Level.WARNING, "unexpected charset: "+charSet+ " should be "+ Charset.defaultCharset());
                //throw new RuntimeException("other charsets than " + Charset.defaultCharset() + " not allowed in this implementation! charset:" + charSet);
            }
            result = new MPTextEntry(type, contentType.mimeType, name,
                    new String(content.subSequence(endOfHeader + HttpConstants.CRLFCRLF_BYTE.length)));
        }
        return result;



    }
}
