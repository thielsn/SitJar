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
 *  Description of StrictSITEnumMap
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @date 07.05.2012
 */
package sit.sstl;

import java.util.EnumMap;

/**
 *
 * @param <T> the enum containing the types
 * @param <Q> the Class implementing StrictSITEnumContainer containing the content for the enum types
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class StrictSITEnumMap<T extends Enum<T>, Q extends StrictSITEnumContainer> extends EnumMap<T, Q> {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param type class of the Enum Type
     * @param values values of the map implementing StrictSITEnumContainer
     */
    public StrictSITEnumMap(Class type, final Q[] values) {
        super(type);

        for (int i = 0; i < values.length; i++) {
            this.put((T) values[i].getEnumType(), values[i]);
        }

    }

    public T lookUp(Q value) {

        return (T) value.getEnumType();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Entry<T, Q> entry : entrySet()) {
            result.append(entry.getKey()).append(" <--> ").append(entry.getValue()).append("\n");
        }
        return result.toString();

    }

    
    public Q get(T key) {
        return super.get(key);
    }
    
    
}
