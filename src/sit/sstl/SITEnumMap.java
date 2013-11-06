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

/**
 *  @author Simon Thiel <simon.thiel at gmx.de>
 */
package sit.sstl;

import java.util.EnumMap;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class SITEnumMap<T extends Enum<T>, Q> extends EnumMap<T, Q> {

    public SITEnumMap(Class type, final Enum[] keys, final Q[] values) {

        super(type);
        if (keys.length != values.length){
            throw new RuntimeException("keys.length != values.length: (keys/values)"+keys.length+"/"+values.length);
        }


        for (int i = 0; i < keys.length; i++) {
            this.put((T) keys[i], values[i]);
        }

    }

    public T lookUp(Q value) {

        for (Entry<T, Q> entry : entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Entry<T, Q> entry : entrySet()) {
            result.append(entry.getKey()).append(" <--> ").append(entry.getValue()).append("\n");
        }
        return result.toString();

    }


}
