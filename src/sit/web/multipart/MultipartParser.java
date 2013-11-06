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

    public static MultipartContainer parse(String pure_boundary, Charset charSet, byte[] payload) throws UnsupportedEncodingException {

        MultipartContainer result = new MultipartContainer(pure_boundary);
        
        //##CHARSET_MARKER##        
        byte[] part_boundary = result.getPart_boundary().getBytes(charSet);
        ByteBuilder content = new ByteBuilder(payload);


        int oldIndex = part_boundary.length + HttpConstants.CRLF_BYTE.length;

        int index;
        while (-1 != (index = content.indexOf(oldIndex, part_boundary))) {


        result.addPart(parsePart(charSet, content.subSequence(oldIndex, index-HttpConstants.CRLF_BYTE.length)));    
        oldIndex = index + part_boundary.length + HttpConstants.CRLF_BYTE.length;
        
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
        //##CHARSET_MARKER##
        
        String header = new String(content.subSequence(0, endOfHeader), charSet);

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
            
            //##CHARSET_MARKER##
            result = new MPTextEntry(type, contentType.mimeType, name,
                    new String(content.subSequence(endOfHeader + HttpConstants.CRLFCRLF_BYTE.length),charSet));
        }
        return result;



    }
}
