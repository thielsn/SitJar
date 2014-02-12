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
 * along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>. * 
 */
package sit.db.generator;

import java.lang.reflect.Method;
import sit.db.datastructure.DataStructure;
import sit.tools.StringFormat;

/**
 *
 * @author simon
 * @param <T>
 */
public class TableMapEntry<T> {

    final String tableName;
    final String dataStructureName;
    final String dataStructurePackage;
    final T dataStructure;

    public TableMapEntry(String tableName, String dataStructureName, String dataStructurePackage, T dataStructure) {
        this.tableName = tableName;
        this.dataStructureName = dataStructureName;
        this.dataStructurePackage = dataStructurePackage;
        this.dataStructure = dataStructure;
    }

    T getDataStructure() {
        return dataStructure;
    }

    String guessGetterForDBEntry(String dbFieldName) {
        Method method;
        //first try exact match
        if ((method = findGetter(dbFieldName, true))!=null) {
            return method.getName();
        }//else
        // try guessing match
        if ((method = findGetter(dbFieldName, false))!=null) {
            return method.getName();
        }//else

        return "get"+StringFormat.capitalizeFirstLetter(dbFieldName);
    }

    private Method findGetter(String dbFieldName, boolean exactMatch) throws SecurityException {
        for (Method method : dataStructure.getClass().getMethods()) {
            try {
                if ((method.getName().startsWith("get") 
                        && matches(method.getName().substring(3), dbFieldName, exactMatch))
                        ||( method.getName().startsWith("is")&&
                        (matches(method.getName().substring(2), dbFieldName, exactMatch)
                        
                        ||( matches(method.getName(), dbFieldName, exactMatch)))
                        )) {
                    return method;
                }
            }catch (IndexOutOfBoundsException ex) {
                //skip this method
            }
        }
        return null;
    }

    private boolean matches(String methodBareName, String dbFieldName, boolean exactMatch) {
        if (exactMatch){
            return methodBareName.toLowerCase().equals(dbFieldName.toLowerCase());
        }//else

        return methodBareName.toLowerCase().contains(dbFieldName.toLowerCase());
    }

    public String guessSetterForDBEntry(String dbFieldName) {

        Method method;
        //first try exact match
        if ((method = findSetter(dbFieldName, true))!=null) {
            return method.getName();
        }//else
        // try guessing match
        if ((method = findSetter(dbFieldName, false))!=null) {
            return method.getName();
        }//else

        return "set"+StringFormat.capitalizeFirstLetter(dbFieldName);
    }

    private Method findSetter(String dbFieldName, boolean exactMatch) throws SecurityException {
        for (Method method : dataStructure.getClass().getMethods()) {
            try {
                if (method.getName().startsWith("set") 
                        && matches(method.getName().substring(3), dbFieldName, exactMatch)) {
                    return method;
                }
            }catch (IndexOutOfBoundsException ex) {
                //skip this method
            }
        }
        return null;
    }

}
