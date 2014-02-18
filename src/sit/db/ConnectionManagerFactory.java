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
package sit.db;

public class ConnectionManagerFactory {

    public static final String MYSQL_CLEANUP_THREAD = "com.mysql.jdbc.AbandonedConnectionCleanupThread";
    private static final String MYSQL_JDBC_DATABASE_DEFAULT_CONNECTOR = "jdbc:mysql://localhost:3306/";
    private static final String MYSQL_DATABASE_DRIVER = "com.mysql.jdbc.Driver";

    public static ConnectionManager createMysqlConnectionManager(String dbconnector, String databaseName, String userName, String password) {
        return new ConnectionManager(
                new DatabaseSetup(
                        MYSQL_DATABASE_DRIVER, MYSQL_CLEANUP_THREAD, dbconnector, databaseName, userName, password));
    }

    public static ConnectionManager createMysqlConnectionManager(String databaseName, String userName, String password) {
        
        return createMysqlConnectionManager(MYSQL_JDBC_DATABASE_DEFAULT_CONNECTOR, databaseName, userName, password);
    }

}
