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
 *  @author Simon Thiel <simon.thiel at gmx.de>
 */

package sit.web;

import java.nio.charset.Charset;

/**
 *
 * @author thiel
 */
public interface HttpConstants {
    /** 2XX: generally "OK" */
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_ACCEPTED = 202;
    public static final int HTTP_NOT_AUTHORITATIVE = 203;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_RESET = 205;
    public static final int HTTP_PARTIAL = 206;
    /** 3XX: relocation/redirect */
    public static final int HTTP_MULT_CHOICE = 300;
    public static final int HTTP_MOVED_PERM = 301;
    public static final int HTTP_FOUND = 302;
    public static final int HTTP_SEE_OTHER = 303;
    public static final int HTTP_NOT_MODIFIED = 304;
    public static final int HTTP_USE_PROXY = 305;
    public static final int HTTP_TEMPORARY_REDIRECT = 307;
    /** 4XX: client error */
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_PAYMENT_REQUIRED = 402;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_BAD_METHOD = 405;
    public static final int HTTP_NOT_ACCEPTABLE = 406;
    public static final int HTTP_PROXY_AUTH = 407;
    public static final int HTTP_CLIENT_TIMEOUT = 408;
    public static final int HTTP_CONFLICT = 409;
    public static final int HTTP_GONE = 410;
    public static final int HTTP_LENGTH_REQUIRED = 411;
    public static final int HTTP_PRECON_FAILED = 412;
    public static final int HTTP_ENTITY_TOO_LARGE = 413;
    public static final int HTTP_REQ_TOO_LONG = 414;
    public static final int HTTP_UNSUPPORTED_TYPE = 415;
    
    public static final int HTTP_TOO_MANY_REQUESTS = 429;
    /** 5XX: server error */
    public static final int HTTP_SERVER_ERROR = 500;
    public static final int HTTP_INTERNAL_ERROR = 501;
    public static final int HTTP_BAD_GATEWAY = 502;
    public static final int HTTP_UNAVAILABLE = 503;
    public static final int HTTP_GATEWAY_TIMEOUT = 504;
    public static final int HTTP_VERSION = 505;


    public final static String HTTP_COMMAND_HEAD = "HEAD";
    public final static String HTTP_COMMAND_GET = "GET";
    public final static String HTTP_COMMAND_POST = "POST";
    public final static String HTTP_COMMAND_PUT = "PUT";
    public final static String HTTP_COMMAND_DELETE = "DELETE";

    public final static String HTTP_HEADER_FIELD_CONTENT_LENGTH = "Content-Length:";

    public final static String CRLF = "\r\n";
    public final static String CRLFCRLF = CRLF + CRLF;
    public static final byte[] CRLF_BYTE = {(byte) '\r', (byte) '\n'};
    public static final byte[] CRLFCRLF_BYTE = {(byte) '\r', (byte) '\n', (byte) '\r', (byte) '\n'};
 
    //public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
    public static final Charset DEFAULT_CHARSET = Charset.forName("US-ASCII");
    
    public static final String DEFAULT_MIME_TYPE = "text/html";
    public static final String MULTIPART_MIME_TYPE = "multipart/form-data";
    
    public static final String MIME_APPLICATION_OCTETSTREAM = "application/octet-stream";
   
    public static final String SUB_FIELD_SEPARATOR = "; ";
    
    public static final String CONTENT_DISPOSITION_TAG = "Content-Disposition: form-data; ";
    public static final String FILENAME_DISPOSITION_TAG = "filename=";
    public static final String NAME_DISPOSITION_TAG = "name=";

    public static final String CONTENT_LENGTH_TAG = "Content-Length: ";
    
    public static final String CONTENT_TYPE_TAG = "Content-Type: ";
    public static final String CHARSET_CONTENT_TYPE_TAG = "charset=";
    public static final String BOUNDARY_CONTENT_TYPE_PREFIX = "boundary=";    
    
    
    public static final String CONTENT_TRANSFER_ENCODING_BINARY_TAG = "Content-Transfer-Encoding: binary";
    
}
