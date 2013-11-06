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

/*
 *
 * Helper Tool to access some SQL databases - currently MSAccess and Postgres
 * 
 */
package sit.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.Pair;

/**
 * most of the functions are tested with postgreSQL
 * TODO: however future plans are to support
 * multiple SQL dialects by replacing the language specific parts by the fitting
 * language snippets -
 *
 * @author thiel
 */
public class SQLHelper {

    public Connection initAccessConnection(String dbName) throws SQLException {
        try {
            // Load the database driver
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            // Get a connection to the database
            Connection conn = DriverManager.getConnection("jdbc:odbc:" + dbName);
            // Print all warnings
            for (SQLWarning warn = conn.getWarnings(); warn != null; warn = warn.getNextWarning()) {
                System.out.println("SQL Warning:");
                System.out.println("State  : " + warn.getSQLState());
                System.out.println("Message: " + warn.getMessage());
                System.out.println("Error  : " + warn.getErrorCode());
            }
            return conn;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Connection initPostgresConnection(String dbName,
            String user, String passwd) throws SQLException {

        return initPostgresConnectionWithURL(
                "jdbc:postgresql://localhost:5432/" + dbName,
                user,
                passwd);
    }

    public Connection initPostgresConnectionWithURL(String url,
            String user, String passwd) throws SQLException {
        try {

            //load driver
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, passwd);

            // Print all warnings
            for (SQLWarning warn = conn.getWarnings(); warn != null; warn = warn.getNextWarning()) {
                System.out.println("SQL Warning:");
                System.out.println("State  : " + warn.getSQLState());
                System.out.println("Message: " + warn.getMessage());
                System.out.println("Error  : " + warn.getErrorCode());
            }


            return conn;


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public SQLResultContainer getSpecificTableEntry(Connection conn, String tablename,
            String field, String value) throws SQLException {

        return getSpecificTableEntry(conn, tablename, field, value, ResultSet.CONCUR_READ_ONLY);
    }

    public SQLResultContainer getSpecificTableEntry(Connection conn, String tablename,
            String field, String value, int resultSetConCur) throws SQLException {

        

        return executeSQLStatement(conn, "SELECT * FROM " + tablename
                + " WHERE " + field + " = '" + value + "'",
                ResultSet.TYPE_FORWARD_ONLY, resultSetConCur);
    }

    public void dumpTableToFile(Connection conn, String tableName, String fileName) throws SQLException {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileOutputStream(fileName));
            writeTableToStream(conn, tableName, writer);
            writer.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void writeTableToStream(Connection conn, String tableName, PrintWriter writer) throws SQLException {

        SQLResultContainer rc = executeSQLStatement(conn, "SELECT * FROM " + tableName);
        ResultSet rs = rc.getResultSet();
        ResultSetMetaData rsm = rs.getMetaData();


        for (int i = 1; i <= rsm.getColumnCount(); i++) {
            if (i > 1) {
                writer.print(";");
            }
            writer.print(rsm.getColumnName(i));

        }
        writer.println("");//EOL
        while (rs.next()) {

            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                if (i > 1) {
                    writer.print(";");
                }
                String entry = rs.getString(i);
                if (entry == null) {
                    entry = "";
                }
                writer.print((entry.replaceAll(";", "")));
            }
            writer.println("");//EOL
        }

        // Close the result set, statement and the connection
        rc.close();


    }

    public SQLResultContainer executeSQLStatement(Connection conn, String statement) throws SQLException {
        return executeSQLStatement(conn, statement, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
    }

    public SQLResultContainer executeSQLStatement(Connection conn, String statement,
            int resultSetType, int resultSetConCur) throws SQLException {
        // Get a statement from the sconnection
        Statement stmt = conn.createStatement(resultSetType, resultSetConCur);

        // Execute the query
        ResultSet rs = stmt.executeQuery(statement);

        return new SQLResultContainer(stmt, rs);
    }

    public void createTable(Connection conn, String tableName,
            Vector<Pair<String, String>> fields, boolean firstFieldIsPrimKey) throws SQLException {


        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                "trying to delete table:" + tableName);
        Statement connStatement = conn.createStatement();
        try {
            connStatement.execute("DROP TABLE " + tableName + ";");
            connStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                    "table did not exist ...");
        }

        //create table
        connStatement = conn.createStatement();



        String createTableString = "CREATE TABLE '" + tableName + "' (";
        String primaryKey = null;

        int counter = 0;
        for (Pair<String, String> field : fields) {

            if (counter == 0) {
                primaryKey = field.getA();
            }
            if (counter > 0) {
                createTableString += ", ";
            }
            createTableString += "'"+field.getA() + "' " + field.getB();

            counter++;
        }
        if (primaryKey != null && firstFieldIsPrimKey) {
            createTableString += ", CONSTRAINT " + tableName + "_pk PRIMARY KEY (" + primaryKey + ")";
        }
        createTableString += ");";

        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "creating table...");
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, createTableString);
        connStatement.execute(createTableString);
        connStatement.close();
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "done.");


        connStatement.close();
    }

    public void addLineToTable(Connection conn, String tableName,
            Vector<String> line) throws SQLException {


        String insertString = "INSERT INTO " + tableName + " VALUES (";
        for (int i = 0; i < line.size(); i++) {
            if (i > 0) {
                insertString += ",";
            }

            String entry = line.get(i);
            if (entry == null) {
                entry = "";
            }
            entry = entry.replaceAll("'", "");
            if (entry.length() == 0) {
                insertString += "' '";
            } else {
                insertString += "'" + entry + "'";

            }

        }
        insertString += ");";

        Statement targetStatement = conn.createStatement();
        if (targetStatement.executeUpdate(insertString) != 1) {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
                    "insert failed - exiting");
            targetStatement.close();

            return;
        }
        targetStatement.close();

    }

    public boolean tableExsists(Connection conn, String tablename) throws SQLException{
        String sqlStr = "SELECT  table_name FROM information_schema.system_tables WHERE table_name = '"+tablename+"';";

        SQLResultContainer rc =  executeSQLStatement(conn, sqlStr,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        return rc.getResultSet().next();

    }

    public void updateOrAddDataSet(Connection conn, String tablename,
            String idField, Hashtable<String, String> dataSet) throws SQLException {

        updateOrAddDataSet(conn, tablename, idField, dataSet, false);
    }

    public void createCLOBTable(Connection conn, String tablename,
            String idField, Hashtable<String, String> dataSet) throws SQLException{

        Vector<Pair<String, String>> fields = new Vector();
        for (String key: dataSet.keySet()){
            Pair<String, String> field = new Pair(key,"CLOB");
            if (key.equalsIgnoreCase(idField)){ //put primary key to the head of the vector
                fields.add(0, field);
            }else{
                fields.add(field);
            }
        }
        createTable(conn, tablename, fields, false);
    }

    public void updateOrAddDataSet(Connection conn, String tablename,
            String idField, Hashtable<String, String> dataSet, boolean addTableIfNotExists) throws SQLException {

        //check for table existing first
        if (addTableIfNotExists){
            if (!tableExsists(conn, tablename)){
                createCLOBTable(conn, tablename, idField, dataSet);
            }
        }


        //find id field
        SQLResultContainer rc = null;
        try{
            rc = getSpecificTableEntry(conn, tablename, idField, dataSet.get(idField));
        }catch (SQLException ex){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "", ex);
            System.out.println("state:"+ex.getSQLState());

        }


        if (rc.getResultSet().next()) {//if we have an existing entry

            for (Entry<String, String> entry : dataSet.entrySet()) {
                if (idField.equalsIgnoreCase(entry.getKey())) {
                    //do nothing this should match the id anyway
                } else {
                    rc.getResultSet().updateString(entry.getKey(), entry.getValue());
                }
                rc.getResultSet().updateRow();

            }


        } else { //no existing entry found ...

            //sort fields according             
            Vector<String> line = new Vector();
            for (int i = 1; i <= rc.getResultSet().getMetaData().getColumnCount(); i++) {
                String columnName = rc.getResultSet().getMetaData().getColumnName(i);
                line.add(dataSet.get(columnName));

            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, line.toString());
            addLineToTable(conn, tablename, line);
        }
        rc.close();

    }

      public Vector<Hashtable<String, String>> getAllKeyValueSets(Connection conn, String tablename) throws SQLException {

        Vector<Hashtable<String, String>> result = new Vector();


        SQLResultContainer rc =  executeSQLStatement(conn, "SELECT * FROM " + tablename,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        ResultSetMetaData columnNames = rc.getResultSet().getMetaData();
        int columnCount = columnNames.getColumnCount();

        while (rc.getResultSet().next()==true) {
            Hashtable<String, String> dataSet = new Hashtable();
            for (int i = 1; i <= columnCount; i++) {
                dataSet.put(
                        columnNames.getColumnName(i),
                        rc.getResultSet().getString(i));
            }
            result.add(dataSet);
        }
        rc.close();
        return result;
    }

    public Hashtable<String, String> getKeyValueSet(Connection conn, String tablename,
            String field, String value) throws SQLException {

        Hashtable<String, String> result = new Hashtable();


        SQLResultContainer rc = getSpecificTableEntry(conn, tablename, field, value);

        if (rc.getResultSet().next()) {
            for (int i = 1; i <= rc.getResultSet().getMetaData().getColumnCount(); i++) {
                result.put(
                        rc.getResultSet().getMetaData().getColumnName(i),
                        rc.getResultSet().getString(i));
            }
        }
        rc.close();
        return result;
    }

    public void transferTable(Connection source, Connection target,
            String tableName, boolean firstFieldIsPrimKey) throws SQLException {

        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                "transfering from {0} to {1} table:{2}",
                new Object[]{source.getMetaData().getDatabaseProductName(),
                    target.getMetaData().getDatabaseProductName(), tableName});

        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                "trying to delete table:" + tableName);
        Statement targetStatement = target.createStatement();
        try {
            targetStatement.execute("DROP TABLE " + tableName + ";");
            targetStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                    "table did not exist ...");
        }

        //create table
        targetStatement = target.createStatement();
        SQLResultContainer rc = executeSQLStatement(source, "SELECT * FROM " + tableName);
        ResultSet rs = rc.getResultSet();
        ResultSetMetaData rsm = rs.getMetaData();

        String createTableString = "CREATE TABLE " + tableName + " (";
        String primaryKey = null;
        for (int i = 1; i <= rsm.getColumnCount(); i++) {
            if (i == 1) {
                primaryKey = rsm.getColumnName(i);
            }
            if (i > 1) {
                createTableString += ", ";
            }
            createTableString += rsm.getColumnName(i) + " " + rsm.getColumnTypeName(i);

        }
        if (primaryKey != null && firstFieldIsPrimKey) {
            createTableString += ", CONSTRAINT " + tableName + "_pk PRIMARY KEY (" + primaryKey + ")";
        }
        createTableString += ");";
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "creating table...");
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, createTableString);
        targetStatement.execute(createTableString);
        targetStatement.close();
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "done.");

        //insert entries
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "inserting entries - please wait...");
        int counter = 0;
        while (rs.next()) {

            String insertString = "INSERT INTO " + tableName + " VALUES (";
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                if (i > 1) {
                    insertString += ",";
                }

                String entry = rs.getString(i);
                if (entry == null) {
                    entry = "";
                }
                entry = entry.replaceAll("'", "");
                if (entry.equals("")) {
                    insertString += "' '";
                } else {
                    insertString += "'" + entry + "'";

                }

            }
            insertString += ");";

            targetStatement = target.createStatement();
            if (targetStatement.executeUpdate(insertString) != 1) {
                Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
                        "insert failed - exiting");
                targetStatement.close();
                rc.close();
                return;
            }
            targetStatement.close();
            counter++;
        }

        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                "inserted " + (counter + 1) + " entries.");


        rc.close();
        targetStatement.close();

    }
}
