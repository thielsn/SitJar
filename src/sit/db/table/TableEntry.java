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

package sit.db.table;

import sit.sstl.StrictSITEnumContainer;

/**
 *
 * @author simon
 * @param <T>
 * @param <TABLE_FIELDS> extends Enum< TABLE_FIELDS>
 */
public class TableEntry<T, TABLE_FIELDS extends Enum< TABLE_FIELDS> > implements StrictSITEnumContainer<TABLE_FIELDS>{
    public static final int PRIMKEY = 1;
    public static final int PRIMKEY_AUTOGEN = 4;
    
    private final TABLE_FIELDS fieldNameType;
    private final String name;
    private final TABLE_ENTRY_TYPE dbType;
    private final Mapper<T> mapper;
    private final int flag;

    public TableEntry(TABLE_FIELDS fieldNameType, String name, TABLE_ENTRY_TYPE dbType, Mapper<T> mapper) {
        this(fieldNameType, name, dbType, mapper, 0);
    }


    public TableEntry(TABLE_FIELDS fieldNameType, String name, TABLE_ENTRY_TYPE dbType, Mapper<T> mapper, int flag) {
        this.fieldNameType = fieldNameType;
        this.name = name;
        this.dbType = dbType;
        this.mapper = mapper;
        this.flag = flag;
    }






    boolean isPrimeKey() {
        return (PRIMKEY & flag) == PRIMKEY;
    }

    boolean isPrimeKeyAutogen() {
        return (PRIMKEY_AUTOGEN & flag) == PRIMKEY_AUTOGEN;
    }

    @Override
    public TABLE_FIELDS getEnumType() {
        return fieldNameType;
    }

    /**
     * @return the fieldNameType
     */
    public TABLE_FIELDS getFieldNameType() {
        return fieldNameType;
    }

    /**
     * @return the dbType
     */
    public TABLE_ENTRY_TYPE getDbType() {
        return dbType;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the mapper
     */
    public Mapper<T> getMapper() {
        return mapper;
    }

}
