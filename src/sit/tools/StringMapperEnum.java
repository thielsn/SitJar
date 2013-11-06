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
package sit.tools;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Just an example for an intelligent enum map with lookup function
 * Easier would be to use the StringEnumMap for general use
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public enum StringMapperEnum {

    ONE("Eins"),
    TWO("Zwei"),
    THREE("Drei");
    private static final Map<String, StringMapperEnum> lookup = new HashMap();

    static {
        for (StringMapperEnum s : StringMapperEnum.values()) {
            lookup.put(s.getString(), s);
        }
    }
    private String myString;

    private StringMapperEnum(String myString) {
        this.myString = myString;
    }

    public String getString() {
        return myString;
    }

    public static StringMapperEnum get(String myString) {
        return lookup.get(myString);
    }
}
