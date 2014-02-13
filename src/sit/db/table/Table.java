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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
 * TODO remove filter from sql-string and use prepared statement parameter instead!!!
 *
 * @author simon
 * @param <T> ParticepsDataType
 * @param <TABLE_FIELDS>
 */
public abstract class Table<T extends DataStructure, TABLE_FIELDS extends Enum< TABLE_FIELDS>> {

    private final StrictSITEnumMap<TABLE_FIELDS, TableEntry<T, TABLE_FIELDS>> entries;
    private final boolean verbose = false;

    public Table(StrictSITEnumMap<TABLE_FIELDS, TableEntry<T, TABLE_FIELDS>> entries) {
        this.entries = entries;
        validateEntries();
    }

    private String createInsertString() {
        StringBuilder result = new StringBuilder("INSERT INTO ");
        StringBuilder params = new StringBuilder();
        result.append(getTableName()).append(" (");

        boolean firstEntry = true;
        for (TableEntry entry : entries.values()) {
            if (entry.isPrimeKeyAutogen()) {
                //skip this
                continue;
            }
            if (firstEntry) {
                firstEntry = false;
                params.append("?");
            } else {
                result.append(", ");
                params.append(",?");
            }
            result.append(entry.getName());
        }
        result.append(") VALUES(")
                .append(params)
                .append(");");
        if (verbose) {
            Logger.getLogger(Table.class.getName()).log(Level.INFO, result.toString());
        }
        return result.toString();
    }

    private String createUpdateString(Map<TABLE_FIELDS, String> filter) throws DBException {
        StringBuilder result = new StringBuilder("UPDATE ");
        result.append(getTableName())
                .append(" SET ");

        boolean firstEntry = true;
        for (TableEntry entry : entries.values()) {
            if (entry.isPrimeKeyAutogen()) {
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
        result.append("=?");
        createSQLCondition(filter, result);
        result.append(";");
        if (verbose) {
            Logger.getLogger(Table.class.getName()).log(Level.INFO, result.toString());
        }
        return result.toString();
    }

    private String createGetAllString(Map<TABLE_FIELDS, String> filter) {
        StringBuilder result = new StringBuilder("SELECT * FROM ");

        result.append(getTableName());
        createSQLCondition(filter, result);

        result.append(";");
        if (verbose) {
            Logger.getLogger(Table.class.getName()).log(Level.INFO, result.toString());
        }
        return result.toString();
    }

    private void createSQLCondition(Map<TABLE_FIELDS, String> filter, StringBuilder result) {
        boolean firstEntry = true;
        for (Map.Entry<TABLE_FIELDS, String> filterEntry : filter.entrySet()) {
            if (firstEntry) {
                firstEntry = false;
                result.append(" WHERE");
            } else {
                result.append(" AND");
            }
            result.append(" ").append(createSQLEquals(filterEntry));
        }
    }

    private String createDeleteString(Map<TABLE_FIELDS, String> filter) {
        StringBuilder result = new StringBuilder("DELETE FROM ");

        result.append(getTableName());
        createSQLCondition(filter, result);

        result.append(";");
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
        int stmtIndex = 1;
        for (TableEntry entry : entries.values()) {

            if (entry.isPrimeKeyAutogen()) {
                //skip this - will be generated automatically
                continue;
            }//else

            setStatementEntry(stmt, dataStructureEntry, entry, stmtIndex);
            stmtIndex++;

        }
        
        stmt.execute();
        T result = (T) dataStructureEntry.getClone();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs != null && rs.next()) {
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

    public boolean deleteEntry(ConnectionManager db, T dataStructure) throws SQLException {
        Connection con = db.getConnection();
        try {
            String sqlString = createDeleteString(createFilterFromDataStructure(dataStructure));
            PreparedStatement stmt = con.createPrepStmt(sqlString);
            return (stmt.execute());
        } finally {
            //stmt.close() done by ConnectionManager
            db.returnConnection(con);
        }
    }

    public List<T> getEntries(ConnectionManager db, Map<TABLE_FIELDS, String> filter) throws SQLException {
        Connection con = db.getConnection();
        List<T> result = new ArrayList();
        try {
            String sqlString = createGetAllString(filter);
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

    public List<T> getAllEntries(ConnectionManager db) throws SQLException {
        return getEntries(db, new HashMap());
    }

    public T updateEntry(ConnectionManager db, T dataStructure) throws SQLException, DBException {
        Connection con = db.getConnection();

        try {
            String sqlString = createUpdateString(createFilterFromDataStructure(dataStructure));
            PreparedStatement stmt = con.createPrepStmt(sqlString);
            int stmtCounter = 1;
            for (TableEntry entry : entries.values()) {
                if (entry.isPrimeKeyAutogen()) {
                    continue;//skip this
                }
                setStatementEntry(stmt, dataStructure, entry, stmtCounter);
                stmtCounter++;
            }
            stmt.execute();//UPDATE seems not to return any resultset
            return dataStructure;

        } finally {
            //stmt.close() done by ConnectionManager
            db.returnConnection(con);
        }

    }

    public abstract String getTableName();

    protected abstract T createNewInstance();

    public String getTag() {
        return createNewInstance().getTag();
    }

    private T getDataStructureFromDBEntry(ResultSet rs) throws SQLException {

        T result = createNewInstance();
        int rsCounter = 1;
        for (TableEntry entry : entries.values()) {
            setDatastructureEntry(result, rs, entry, rsCounter);
            rsCounter++;
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
        int primeKeyCounter = 0;
        for (TableEntry tableEntry : entries.values()) {
            if (tableEntry.isPrimeKey()) {
                primeKeyCounter++;
            }
        }
        if (primeKeyCounter > 1) {
            throw new RuntimeException("More then one PrimeKey defined for table: " + getTableName());
        }
        if (primeKeyCounter == 0) {
            Logger.getLogger(Table.class.getName()).log(Level.WARNING, "No PrimeKey defined for table: " + getTableName());
        }
    }

    public TableEntry<T, TABLE_FIELDS> getPrimeKeyEntry() {
        for (TableEntry<T, TABLE_FIELDS> tableEntry : entries.values()) {
            if (tableEntry.isPrimeKey()) {
                return tableEntry;
            }
        }
        return null;
    }

    public boolean hasPrimeKey() {
        return getPrimeKeyEntry() != null;
    }

    private String createSQLEquals(Map.Entry<TABLE_FIELDS, String> filterEntry) {
        TableEntry<T, TABLE_FIELDS> entry = entries.get(filterEntry.getKey());

        if (entry.getDbType() == TABLE_ENTRY_TYPE.BYTES) {
            throw new RuntimeException("SQL search for byte arrays is not supported!");
        }
        String result = entry.getName() + " = ";
        if (entry.getDbType() == TABLE_ENTRY_TYPE.STRING
                || entry.getDbType() == TABLE_ENTRY_TYPE.DATE
                || entry.getDbType() == TABLE_ENTRY_TYPE.TIMESTAMP) {
            result += "'" + filterEntry.getValue();
        } else {
            result += filterEntry.getValue();
        }
        return result;
    }

    public Map<TABLE_FIELDS, String> createFilterFromId(int id) {
        Map<TABLE_FIELDS, String> result = new LinkedHashMap();
        result.put(getPrimeKeyEntry().getFieldNameType(), id + "");
        return result;
    }

    private Map<TABLE_FIELDS, String> createFilterFromDataStructure(T dataStructure) {

        if (hasPrimeKey()) {
            return createFilterFromId(dataStructure.getId());
        }//else no primekey defined

        Map<TABLE_FIELDS, String> result = new LinkedHashMap();
        //create all field filter (except for bytes)
        for (TableEntry<T, TABLE_FIELDS> tableEntry : entries.values()) {
            if (tableEntry.getDbType() != TABLE_ENTRY_TYPE.BYTES) {
                result.put(tableEntry.getEnumType(),
                        tableEntry.getMapper().getAsStringRepresentation(dataStructure, tableEntry.getDbType()));
            }
        }
        return result;
    }

}
