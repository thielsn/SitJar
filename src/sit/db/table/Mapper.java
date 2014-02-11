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

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author simon
 */
public class Mapper<T> {
    //BOOLEAN, INT, LONG, BYTES, FLOAT, DOUBLE, STRING, DATE, TIMESTAMP

    public void setBoolean(T dataStructureEntry, boolean value){
        throw new UnsupportedOperationException("Override for using...");
    }
    public void setInt(T dataStructureEntry, int value){
        throw new UnsupportedOperationException("Override for using...");
    }
    public void setLong(T dataStructureEntry, long value){
        throw new UnsupportedOperationException("Override for using...");
    }
    public void setBytes(T dataStructureEntry, byte[] value){
        throw new UnsupportedOperationException("Override for using...");
    }
    public void setFloat(T dataStructureEntry, float value){
        throw new UnsupportedOperationException("Override for using...");
    }
    public void setDouble(T dataStructureEntry, double value){
        throw new UnsupportedOperationException("Override for using...");
    }
    public void setString(T dataStructureEntry, String value){
        throw new UnsupportedOperationException("Override for using...");
    }
    public void setDate(T dataStructureEntry, Date value){
        throw new UnsupportedOperationException("Override for using...");
    }
    public void setTimestamp(T dataStructureEntry, Timestamp value){
        throw new UnsupportedOperationException("Override for using...");
    }

    public boolean getBoolean(T dataStructureEntry) {
        throw new UnsupportedOperationException("Override for using...");
    }

    public int getInt(T dataStructureEntry) {
        throw new UnsupportedOperationException("Override for using...");
    }

    public long getLong(T dataStructureEntry) {
        throw new UnsupportedOperationException("Override for using...");
    }

    public byte[] getBytes(T dataStructureEntry) {
        throw new UnsupportedOperationException("Override for using...");
    }

    public float getFloat(T dataStructureEntry) {
        throw new UnsupportedOperationException("Override for using...");
    }

    public double getDouble(T dataStructureEntry) {
        throw new UnsupportedOperationException("Override for using...");
    }

    public String getString(T dataStructureEntry) {
        throw new UnsupportedOperationException("Override for using...");
    }

    public Date getDate(T dataStructureEntry) {
        throw new UnsupportedOperationException("Override for using...");
    }

    public Timestamp getTimestamp(T dataStructureEntry) {
        throw new UnsupportedOperationException("Override for using...");
    }

    public String getAsStringRepresentation(T dataStructureEntry, TABLE_ENTRY_TYPE dbType){
         if (dbType == TABLE_ENTRY_TYPE.BOOLEAN) {
            return getBoolean(dataStructureEntry ) + "";
        } else if (dbType == TABLE_ENTRY_TYPE.BYTES) {
            return getBytes(dataStructureEntry ) + "";
        } else if (dbType == TABLE_ENTRY_TYPE.DATE) {
            return getDate(dataStructureEntry ).toString();
        } else if (dbType == TABLE_ENTRY_TYPE.DOUBLE) {
            return getDouble(dataStructureEntry ) + "";
        } else if (dbType == TABLE_ENTRY_TYPE.FLOAT) {
            return getFloat(dataStructureEntry ) + "";
        } else if (dbType == TABLE_ENTRY_TYPE.INT) {
            return getInt(dataStructureEntry ) + "";
        } else if (dbType == TABLE_ENTRY_TYPE.LONG) {
            return getLong(dataStructureEntry ) + "";
        } else if (dbType == TABLE_ENTRY_TYPE.STRING) {
            return getString(dataStructureEntry);
        } else if (dbType == TABLE_ENTRY_TYPE.TIMESTAMP) {
            return getTimestamp(dataStructureEntry).toString();
        }
         throw new RuntimeException("Unsupported dbType: "+dbType);
    }

}
