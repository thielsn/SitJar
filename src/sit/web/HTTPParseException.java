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

/**
 *
 * @author thiel
 */
public class HTTPParseException extends Exception {

    /**
     * Creates a new instance of <code>UnsupportedHTTPMethod</code> without detail message.
     */
    public HTTPParseException() {
    }


    /**
     * Constructs an instance of <code>UnsupportedHTTPMethod</code> with the specified detail message.
     * @param msg the detail message.
     */
    public HTTPParseException(String msg) {
        super(msg);
    }
}
