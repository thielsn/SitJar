/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.sstl.Pair;
/**
 *
 * @author thiel
 */
public class SQLHelper {

     public Connection initAccessConnection(String dbName) throws SQLException {
        try {
            // Load the database driver
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            // Get a connection to the database
            Connection conn = DriverManager.getConnection("jdbc:odbc:"+dbName);
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
                            String user, String passwd) throws SQLException{
         
         return initPostgresConnectionWithURL(
                 "jdbc:postgresql://localhost:5432/"+dbName,
                 user,
                 passwd);
    }

     public Connection initPostgresConnectionWithURL(String url,
                           String user, String passwd) throws SQLException{
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
            Vector<Pair<String, String> > fields, boolean firstFieldIsPrimKey) throws SQLException{


        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                "trying to delete table:"+tableName);
        Statement connStatement = conn.createStatement();
        try{
            connStatement.execute("DROP TABLE "+tableName+";");
            connStatement.close();
        }catch(SQLException ex){
            Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                    "table did not exist ...");
        }

        //create table
        connStatement = conn.createStatement();



        String createTableString = "CREATE TABLE "+tableName+" (";
        String primaryKey = null;

        int counter=0;
        for (Pair<String, String> field : fields){

            if (counter==0){
                primaryKey = field.getA();
            }
            if (counter > 0) {
                createTableString+=", ";
            }
            createTableString+=field.getA()+" "+field.getB();

            counter++;
        }
        if (primaryKey!=null && firstFieldIsPrimKey){
            createTableString += ", CONSTRAINT "+ tableName +"_pk PRIMARY KEY (" + primaryKey + ")";
        }
        createTableString+=");";

        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "creating table...");
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, createTableString);
        connStatement.execute(createTableString);
        connStatement.close();
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "done.");


        connStatement.close();
    }

    public void addLineToTable(Connection conn, String tableName,
            Vector<String> line) throws SQLException{


        String insertString = "INSERT INTO "+tableName+" VALUES (";
        for (int i = 0; i < line.size(); i++) {
            if (i > 0) {
                insertString+=",";
            }

            String entry = line.get(i);
            if (entry == null) {
                entry = "";
            }
            entry = entry.replaceAll("'", "");
            if (entry.equals("")){
                insertString += "' '";
            }else{
                insertString += "'"+entry+"'";

            }

        }
        insertString+=");";

        Statement targetStatement = conn.createStatement();
        if (targetStatement.executeUpdate(insertString)!=1){
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,
                    "insert failed - exiting");
            targetStatement.close();

            return;
        }
        targetStatement.close();

    }

    public void transferTable(Connection source, Connection target,
            String tableName, boolean firstFieldIsPrimKey) throws SQLException{

        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, 
                "transfering from {0} to {1} table:{2}",
                new Object[]{source.getMetaData().getDatabaseProductName(),
                target.getMetaData().getDatabaseProductName(), tableName});
        
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                "trying to delete table:"+tableName);
        Statement targetStatement = target.createStatement();
        try{
            targetStatement.execute("DROP TABLE "+tableName+";");
            targetStatement.close();
        }catch(SQLException ex){
            Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO,
                    "table did not exist ...");
        }

        //create table
        targetStatement = target.createStatement();
        SQLResultContainer rc = executeSQLStatement(source, "SELECT * FROM " + tableName);
        ResultSet rs = rc.getResultSet();
        ResultSetMetaData rsm = rs.getMetaData();

        String createTableString = "CREATE TABLE "+tableName+" (";
        String primaryKey = null;
        for (int i = 1; i <= rsm.getColumnCount(); i++) {
            if (i==1){
                primaryKey = rsm.getColumnName(i);
            }
            if (i > 1) {
                createTableString+=", ";
            }
            createTableString+=rsm.getColumnName(i)+" "+rsm.getColumnTypeName(i);

        }
        if (primaryKey!=null && firstFieldIsPrimKey){
            createTableString += ", CONSTRAINT "+ tableName +"_pk PRIMARY KEY (" + primaryKey + ")";
        }
        createTableString+=");";
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "creating table...");
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, createTableString);
        targetStatement.execute(createTableString);
        targetStatement.close();    
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "done.");

        //insert entries
        Logger.getLogger(SQLHelper.class.getName()).log(Level.INFO, "inserting entries - please wait...");
        int counter=0;
        while (rs.next()) {

            String insertString = "INSERT INTO "+tableName+" VALUES (";
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                if (i > 1) {
                    insertString+=",";
                }

                String entry = rs.getString(i);
                if (entry == null) {
                    entry = "";
                }
                entry = entry.replaceAll("'", "");
                if (entry.equals("")){
                    insertString += "' '";
                }else{
                    insertString += "'"+entry+"'";

                }

            }
            insertString+=");";

            targetStatement = target.createStatement();
            if (targetStatement.executeUpdate(insertString)!=1){
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
                "inserted "+(counter + 1)+" entries.");
        

        rc.close();
        targetStatement.close();

    }
}
