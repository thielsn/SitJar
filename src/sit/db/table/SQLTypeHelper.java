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

import java.sql.Types;

/**
 *
 * @author simon
 */
public class SQLTypeHelper {
    public static TABLE_ENTRY_TYPE mapSQLType(int sqlType){

        if (sqlType==Types.INTEGER){
            return TABLE_ENTRY_TYPE.INT;
        }
        if (sqlType==Types.BIGINT){
            return TABLE_ENTRY_TYPE.LONG;
        }
        if (sqlType==Types.BOOLEAN || sqlType==Types.BIT){
            return TABLE_ENTRY_TYPE.BOOLEAN;
        }
        if (sqlType==Types.FLOAT || sqlType==Types.REAL){
            return TABLE_ENTRY_TYPE.FLOAT;
        }
        if (sqlType==Types.DOUBLE){
            return TABLE_ENTRY_TYPE.DOUBLE;
        }
        if (sqlType==Types.CHAR || sqlType==Types.VARCHAR || sqlType==Types.LONGVARCHAR){
            return TABLE_ENTRY_TYPE.STRING;
        }

        if (sqlType==Types.VARBINARY || sqlType==Types.BINARY || sqlType==Types.LONGVARBINARY){
            return TABLE_ENTRY_TYPE.BYTES;
        }
        if (sqlType==Types.DATE){
            return TABLE_ENTRY_TYPE.DATE;
        }

        if (sqlType==Types.TIMESTAMP){
            return TABLE_ENTRY_TYPE.TIMESTAMP;
        }

        throw new RuntimeException("cannot map java.sql.Types: "+sqlType);
    }
}
