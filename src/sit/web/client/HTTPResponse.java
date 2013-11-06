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
 *  Description of HTTPResponse
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @date 09.04.2012
 */
package sit.web.client;

import java.nio.charset.Charset;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class HTTPResponse {


    
    public String call;
    public String message = "";
    public Charset charset;
    /** http response code from the call */
    public int code = -1;    
    public final byte[] payload;
    public String reply = ""; //TODO switch toy byte array for reply
    


    public HTTPResponse(String call, byte[] payload, Charset charset) {

        this.call = call;
        this.charset = charset;
        this.payload = payload;
    }

    

   public String getPayloadAsString(){
        //##CHARSET_MARKER##
       return new String(payload, charset);
   }

    @Override
    public String toString() {
        String result = "call:" + call;
        result += "\npayload:\n" + getPayloadAsString() + "\ncode:" + code + "\nmessage:\n" + message + "\nreply:\n" + reply;
        return result;
    }

}
