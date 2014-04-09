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

package sit.string;

/**
 *
 * @author simon
 */
public class StringFormat {

    /**
     * Trims the quotes. <p> For example, <ul> <li>("a.b") => a.b <li>("a.b) =>
     * "a.b <li>(a.b") => a.b" </ul>
     *
     * @param value the string may have quotes
     * @return the string without quotes
     */
    public static String trimQuotes(String value) {
        if (value == null) {
            return value;
        }

        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }

        return value;
    }

    public static String capitalizeFirstLetter(String input){
        return input.substring(0, 1).toUpperCase()+input.substring(1);
    }



}
