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

import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {


    public static final int MAX_DB_CONNECTIONS_BEFORE_WARNING = 10;

    private final String databaseUrl;
    private final String user;
    private final String password;

    private int connectionCounter = 0;

    private final Vector<Connection> connections = new Vector();
    private final DatabaseSetup dbSetup;





    /**
     * Constructor
     *
     * @param dbSetup
     * 
     */
    public ConnectionManager(DatabaseSetup dbSetup) {
        this.dbSetup = dbSetup;
        this.databaseUrl = dbSetup.getJdbcConnector() + dbSetup.getDatabaseName();
        this.user = dbSetup.getUserName();
        this.password = dbSetup.getPassword();

        try {
            Class.forName(dbSetup.getDatabaseDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
    }

    private Connection createConnection() {
        try {
            connectionCounter++;
            if (connectionCounter > MAX_DB_CONNECTIONS_BEFORE_WARNING) {
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.WARNING, "Attention - created " + connectionCounter + " database connections! Please check your implementation");
            }
            return new Connection(DriverManager.getConnection(this.databaseUrl, this.user, this.password));

        } catch (SQLException sqle) {
            //System.err.println(sqle);
            throw new RuntimeException("Cannot connect to database!", sqle);
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        Connection result;

        if (connections.isEmpty()) {
            result = createConnection();
        } else {
            result = connections.remove(0);
        }
        return result;
    }

    public synchronized void returnConnection(Connection connection) throws SQLException {
        connection.closeAndClearStatements();
        connections.add(connection);
    }

    public synchronized void shutdown() {
        for (Connection connection : connections) {
            connection.shutdown();
        }

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        try {
            Class<?> cls = Class.forName(dbSetup.getCleanupThreadClassName());
            Method mth = (cls == null ? null : cls.getMethod("shutdown"));
            if (mth != null) {
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO, "MySQL connection cleanup thread shutdown");
                mth.invoke(null);
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO, "MySQL connection cleanup thread shutdown successful");
            }
        } catch (Throwable ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, "[ER] Failed to shutdown SQL connection cleanup thread: " + ex.getMessage(), ex);
        }

    }


}
