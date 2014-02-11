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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.db.Connection;
import sit.db.ConnectionManager;
import sit.db.exception.DBException;
import sit.db.exception.UpdateException;
import sit.db.datastructure.DataStructure;
import sit.sstl.StrictSITEnumMap;

/** 
 *
 * @author simon
 * @param <T> ParticepsDataType
 */
public abstract class Table<T extends DataStructure, TABLE_FIELDS extends Enum< TABLE_FIELDS> > {

    private final StrictSITEnumMap<TABLE_FIELDS, TableEntry<T, TABLE_FIELDS> >entries;

    public Table(StrictSITEnumMap<TABLE_FIELDS, TableEntry<T, TABLE_FIELDS> > entries) {
        this.entries = entries;
        validateEntries();
    }

    private String createInsertString() {
        //"insert into behandlungspfadvorlage_fachabteilung (behandlungspfadvorlage_id, fachabteilung_id) values(?,?);"
        StringBuilder result = new StringBuilder("insert into ");

        result.append(getTableName()).append(" (");

        boolean firstEntry = true;
        for (TableEntry entry : entries.values()) {
            if (firstEntry) {
                firstEntry = false;
            } else {
                result.append(" ");
            }
            result.append(entry.getName());
        }
        result.append(") values(");
        for (int i = 0; i < entries.size(); i++) {
            if (i == 0) {
                result.append("?");
            } else {
                result.append(",?");
            }
        }
        result.append(");");

        return result.toString();
    }

    private String createUpdateString() throws DBException {
        //"update massnahme set bezeichnung=?, isTerminserie=?, kategorie=?,isAnforderung=?,
        //visualisierung_id=?, status=?, aktualisierer_id=?, lastUpdated=?, archivierer_id=?, archived=? where id=?";
        StringBuilder result = new StringBuilder("update ");
        result.append(getTableName())
                .append(" set ");

        boolean firstEntry = true;
        for (TableEntry entry : entries.values()) {
            if (entry.isPrimeKeyAutogen()){
                //skip this
                continue;
            }
            if (firstEntry) {
                firstEntry = false;
            } else {
                result.append("=?, ");
            }
            result.append(entry.getName());
        }
        TableEntry primeKeyEntry = getPrimeKeyEntry();

        result.append("=? where ")
                .append(primeKeyEntry.getName())
                .append(" = ?;");

        return result.toString();
    }

    private String createGetAllString() {
        StringBuilder result = new StringBuilder("select * from ");
        result.append(getTableName()).append(";");
        return result.toString();
    }

    public T createEntry(ConnectionManager db, T entry) throws SQLException {
        Connection con = db.getConnection();
        T result = null;
        try {
            String sqlString = createInsertString();
            result = createEntryInternal(entry, con.createPrepStmt(sqlString, Statement.RETURN_GENERATED_KEYS));
        } finally {
            //stmt.close() done by ConnectionManager
            db.returnConnection(con);
        }
        return result;
    }

    protected T createEntryInternal(T dataStructureEntry, PreparedStatement stmt) throws SQLException {
        int stmtIndex=1;
        for (TableEntry entry : entries.values()) {
            
            if (entry.isPrimeKeyAutogen()) {
                //skip this - will be generated automatically
                continue;
            }//else

            setStatementEntry(stmt, dataStructureEntry, entry, stmtIndex);
            stmtIndex++;

        }
        T result = null;
        stmt.execute();
        if (stmt.execute()) {
            result = (T)dataStructureEntry.getClone();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            result.setId(rs.getInt(1));
        }
        return result;
    }

    protected void setStatementEntry(PreparedStatement stmt, T dataStructureEntry, TableEntry entry, int stmtIndex)
            throws SQLException {
        if (entry.getDbType() == TABLE_ENTRY_TYPE.BOOLEAN) {
            stmt.setBoolean(stmtIndex, entry.getMapper().getBoolean(dataStructureEntry));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.BYTES) {
            stmt.setBytes(stmtIndex, entry.getMapper().getBytes(dataStructureEntry));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.DATE) {
            stmt.setDate(stmtIndex, entry.getMapper().getDate(dataStructureEntry));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.DOUBLE) {
            stmt.setDouble(stmtIndex, entry.getMapper().getDouble(dataStructureEntry));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.FLOAT) {
            stmt.setFloat(stmtIndex, entry.getMapper().getFloat(dataStructureEntry));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.INT) {
            stmt.setInt(stmtIndex, entry.getMapper().getInt(dataStructureEntry));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.LONG) {
            stmt.setLong(stmtIndex, entry.getMapper().getLong(dataStructureEntry));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.STRING) {
            stmt.setString(stmtIndex, entry.getMapper().getString(dataStructureEntry));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.TIMESTAMP) {
            stmt.setTimestamp(stmtIndex, entry.getMapper().getTimestamp(dataStructureEntry));
        }
    }




    public List<T> getAllEntries(ConnectionManager db) throws SQLException {
        Connection con = db.getConnection();
        List<T> result = new ArrayList();
        try {
            String sqlString = createGetAllString();
            PreparedStatement stmt = con.createPrepStmt(sqlString);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(getDataStructureFromDBEntry(rs));
            }

            rs.close();
        } finally {
            //stmt.close() done by ConnectionManager
            db.returnConnection(con);
        }
        return result;
    }


    public T updateEntry(ConnectionManager db, T dataStructure) throws SQLException, DBException{
        Connection con = db.getConnection();
        
        try {
            String sqlString = createUpdateString();
            PreparedStatement stmt = con.createPrepStmt(sqlString, Statement.RETURN_GENERATED_KEYS);
            int stmtCounter = 1;
            for (TableEntry entry : entries.values()) {
                if (entry.isPrimeKeyAutogen()){
                    continue;//skip this
                }
                setStatementEntry(stmt, dataStructure, entry, stmtCounter);
                stmtCounter++;
            }
            //set id for where part
            stmt.setInt(stmtCounter, dataStructure.getId());
            if (stmt.execute()){
                T result = (T) dataStructure.getClone();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                result.setId(rs.getInt(1));
                return result;
            }

        } finally {
            //stmt.close() done by ConnectionManager
            db.returnConnection(con);
        }
        throw new UpdateException(dataStructure.getTag(), "update of "+getTableName()+" failed for id: "+dataStructure.getId(), -1);
    }
//
//    public T deleteEntry(ConnectionManager db, T entry) throws SQLException{
//
//    }
    public abstract String getTableName();

    protected abstract T createNewInstance();

    private T getDataStructureFromDBEntry(ResultSet rs) throws SQLException {

        T result = createNewInstance();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                TableEntry entry = entries.get(i); //FIXME are we sure the order is always correct? - we could lookup the names from entries...
                setDatastructureEntry(result, rs, entry, i+1);
        }

        return result;
    }

    private void setDatastructureEntry(T result, ResultSet rs, TableEntry entry, int resultSetIndex)
            throws SQLException {
        if (entry.getDbType() == TABLE_ENTRY_TYPE.BOOLEAN) {
            entry.getMapper().setBoolean(result, rs.getBoolean(resultSetIndex));

        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.BYTES) {
            entry.getMapper().setBytes(result, rs.getBytes(resultSetIndex));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.DATE) {
            entry.getMapper().setDate(result, rs.getDate(resultSetIndex));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.DOUBLE) {
            entry.getMapper().setDouble(result, rs.getDouble(resultSetIndex));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.FLOAT) {
            entry.getMapper().setFloat(result, rs.getFloat(resultSetIndex));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.INT) {
            entry.getMapper().setInt(result, rs.getInt(resultSetIndex));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.LONG) {
            entry.getMapper().setLong(result, rs.getLong(resultSetIndex));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.STRING) {
            entry.getMapper().setString(result, rs.getString(resultSetIndex));
        } else if (entry.getDbType() == TABLE_ENTRY_TYPE.TIMESTAMP) {
            entry.getMapper().setTimestamp(result, rs.getTimestamp(resultSetIndex));
        }
    }

    private void validateEntries() {
        int primeKeyCounter=0;
        for (TableEntry tableEntry : entries.values()) {
            if (tableEntry.isPrimeKey()){
                primeKeyCounter++;
            }
        }
        if (primeKeyCounter>1){
            throw new RuntimeException("More then one PrimeKey defined for table: "+getTableName());
        }
        if (primeKeyCounter==0){
            Logger.getLogger(Table.class.getName()).log(Level.WARNING, "No PrimeKey defined for table: "+getTableName());
        }
    }

    private TableEntry getPrimeKeyEntry() throws DBException {
        for (TableEntry tableEntry : entries.values()) {
            if (tableEntry.isPrimeKey()){
                return tableEntry;
            }
        }
        throw new DBException(createNewInstance().getTag(), "No primekey defined for table: "+getTableName(), -1);
    }
}
