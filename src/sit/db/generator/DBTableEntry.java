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

import sit.db.table.TABLE_ENTRY_TYPE;

/**
 *
 * @author simon
 */
class DBTableEntry {

    static class DBFlag{
        final String flagString;
        final int flagValue;

        public DBFlag(String flagString, int flagValue) {
            this.flagString = flagString;
            this.flagValue = flagValue;
        }


    }

    final String name;
    final String dbTypeName;
    final TABLE_ENTRY_TYPE dbType;
    final String javaTypeName;
    final String javaGetSetStub;
    final DBFlag flag;



    public DBTableEntry(String name, TABLE_ENTRY_TYPE type, DBFlag flag) {
        this.name = name;
        this.dbTypeName = name.toUpperCase();
        this.dbType = type;
        this.javaTypeName = getJavaTypeStringFromDBType(dbType);
        this.javaGetSetStub =  getJavaGetSetStubStringFromDBType(dbType);
        this.flag = flag;
    }

    private String getJavaTypeStringFromDBType(TABLE_ENTRY_TYPE dbType) {
        if (dbType == TABLE_ENTRY_TYPE.BOOLEAN) {
            return "boolean";
        } else if (dbType == TABLE_ENTRY_TYPE.BYTES) {
            return "byte[]";
        } else if (dbType == TABLE_ENTRY_TYPE.DATE) {
            return "Date";
        } else if (dbType == TABLE_ENTRY_TYPE.DOUBLE) {
            return "double";
        } else if (dbType == TABLE_ENTRY_TYPE.FLOAT) {
            return "float";
        } else if (dbType == TABLE_ENTRY_TYPE.INT) {
            return "int";
        } else if (dbType == TABLE_ENTRY_TYPE.LONG) {
            return "long";
        } else if (dbType == TABLE_ENTRY_TYPE.STRING) {
            return "String";
        } else if (dbType == TABLE_ENTRY_TYPE.TIMESTAMP) {
            return "Timestamp";
        }
         throw new RuntimeException("Unsupported dbType: "+dbType);
    }

    private String getJavaGetSetStubStringFromDBType(TABLE_ENTRY_TYPE dbType) {
         if (dbType == TABLE_ENTRY_TYPE.BOOLEAN) {
            return "Boolean";
        } else if (dbType == TABLE_ENTRY_TYPE.BYTES) {
            return "Bytes";
        } else if (dbType == TABLE_ENTRY_TYPE.DATE) {
            return "Date";
        } else if (dbType == TABLE_ENTRY_TYPE.DOUBLE) {
            return "Double";
        } else if (dbType == TABLE_ENTRY_TYPE.FLOAT) {
            return "Float";
        } else if (dbType == TABLE_ENTRY_TYPE.INT) {
            return "Int";
        } else if (dbType == TABLE_ENTRY_TYPE.LONG) {
            return "Long";
        } else if (dbType == TABLE_ENTRY_TYPE.STRING) {
            return "String";
        } else if (dbType == TABLE_ENTRY_TYPE.TIMESTAMP) {
            return "Timestamp";
        }
         throw new RuntimeException("Unsupported dbType: "+dbType);
    }


}
