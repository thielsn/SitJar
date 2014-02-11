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
        for (Method method : dataStructure.getClass().getMethods()) {
            try {
                if ((method.getName().startsWith("get") 
                        && isSimilar(method.getName().substring(3), dbFieldName))
                ||( method.getName().startsWith("is")&&
                        (isSimilar(method.getName().substring(2), dbFieldName)
                        
                        ||( isSimilar(method.getName(), dbFieldName)))
                )){
                    return method.getName();


                }
            } catch (IndexOutOfBoundsException ex) {
                //skip this method
            }
        }

        return "get"+StringFormat.capitalizeFirstLetter(dbFieldName);
    }

    private boolean isSimilar(String methodBareName, String dbFieldName) {
        return methodBareName.toLowerCase().contains(dbFieldName.toLowerCase());
    }

    public String guessSetterForDBEntry(String dbFieldName) {
        for (Method method : dataStructure.getClass().getMethods()) {
            try {
                if ((method.getName().startsWith("set") 
                        && isSimilar(method.getName().substring(3), dbFieldName))
                ){
                    return method.getName();


                }
            } catch (IndexOutOfBoundsException ex) {
                //skip this method
            }
            
        }
        return "set"+StringFormat.capitalizeFirstLetter(dbFieldName);
    }

}
