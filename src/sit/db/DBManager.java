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

package sit.db;

import java.util.HashMap;

/**
 *
 * @author simon
 */
public class DBManager {



    private final HashMap<DataBaseProp, ConnectionManager> dbs = new HashMap();
    private final DatabaseType defaultType;
    private final String defaultUser;
    private final String defaultPWD;

    private static class DataBaseProp{

        final String name;
        final DatabaseType type;

        public DataBaseProp(String name, DatabaseType type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + (this.type != null ? this.type.hashCode() : 0);
            hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DataBaseProp other = (DataBaseProp) obj;
            if (this.type != other.type) {
                return false;
            }
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            return true;
        }
    }

    public DBManager(DatabaseType defaultType, String defaultUser, String defaultPWD) {
        this.defaultType = defaultType;
        this.defaultUser = defaultUser;
        this.defaultPWD = defaultPWD;
    }

     public DBManager(String defaultUser, String defaultPWD) {
        this.defaultType = DatabaseType.MYSQL;
        this.defaultUser = defaultUser;
        this.defaultPWD = defaultPWD;
    }

    






     public ConnectionManager getDatabase(String databaseName) {
         return getDatabase(databaseName, defaultType);
     }

     public ConnectionManager getDatabase(String databaseName, DatabaseType type) {
         return getDatabase(databaseName, type, defaultUser, defaultPWD);
     }

     public ConnectionManager getDatabase(String databaseName, DatabaseType type, String username, String password) {
        DataBaseProp myDBProp = new DataBaseProp(databaseName, type);
         if (!dbs.containsKey(myDBProp)){
             ConnectionManager myConnectionManager;
             if (type==DatabaseType.MYSQL){
                 myConnectionManager = ConnectionManagerFactory
                         .createMysqlConnectionManager(databaseName, username,password);
             }else{
                 throw new RuntimeException("Unsupported database type: "+type);
             }

             dbs.put(myDBProp, myConnectionManager);
         }
         return dbs.get(myDBProp);
    }
}
