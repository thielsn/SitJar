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

/**
 *
 * @author simon
 */
public class DatabaseSetup {

    private final String databaseDriver;
    private final String cleanupThreadClassName;
    private final String jdbcConnector;

    private final String databaseName;
    private final String userName;
    private final String password;

    public DatabaseSetup(String databaseDriver, String cleanupThreadClassName, String jdbcConnector, String databaseName, String userName, String password) {
        this.databaseDriver = databaseDriver;
        this.cleanupThreadClassName = cleanupThreadClassName;
        this.jdbcConnector = jdbcConnector;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
    }

    
    

    /**
     * @return the databaseDriver
     */
    public String getDatabaseDriver() {
        return databaseDriver;
    }

    /**
     * @return the jdbcConnector
     */
    public String getJdbcConnector() {
        return jdbcConnector;
    }

    /**
     * @return the databaseName
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the cleanupThreadClassName
     */
    public String getCleanupThreadClassName() {
        return cleanupThreadClassName;
    }



}
