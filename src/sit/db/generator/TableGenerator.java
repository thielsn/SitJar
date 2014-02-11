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
package sit.db.generator;

import sit.db.Connection;
import sit.db.exception.DBException;
import sit.db.ConnectionManager;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sit.db.table.SQLTypeHelper;
import sit.db.table.TABLE_ENTRY_TYPE;
import sit.io.FileHelper;

/**
 *
 * @author simon
 */
public class TableGenerator {
    private final String rootDirForGenerateFiles;
    private final String rootPackage;
    private final String tableString = "table";
    private final String controllerString = "controller";
    private final String tableSubPath = "/"+tableString;
    private final String controllerSubPath = "/"+controllerString;
    private final String tablePackage;
    private final String controllerPackage;
    private final FileHelper fh = new FileHelper();
    private final String rootPath;

    public static class TableMapEntry {

        final String tableName;
        final String dataStructureName;

        public TableMapEntry(String tableName, String dataStructureName) {
            this.tableName = tableName;
            this.dataStructureName = dataStructureName;
        }

    }

    class DBTableEntry {

        final String name;
        final String typeName;
        final TABLE_ENTRY_TYPE type;

        public DBTableEntry(String name, TABLE_ENTRY_TYPE type) {
            this.name = name;
            this.typeName = name.toUpperCase();
            this.type = type;

        }

    }

    public TableGenerator(String rootDirForGenerateFiles, String rootPackage, String rootPath) {
        this.rootDirForGenerateFiles = rootDirForGenerateFiles;
        this.rootPackage = rootPackage;
        this.rootPath = rootPath;
        this.tablePackage = rootPackage+"."+tableString;
        this.controllerPackage = rootPackage+"."+controllerString;
        
    }

    

    public void generateTable(final TableMapEntry tableEntry, ConnectionManager db) throws DBException, SQLException {

        System.out.println("generate for table: " + tableEntry.tableName);

        Connection connection = db.getConnection();

        try {
            PreparedStatement stmt = connection.createPrepStmt("Select * from " + tableEntry.tableName);
            if (!stmt.execute()) {
                throw new DBException(tableEntry.tableName, "error when accessing", -1);
            }
            createFiles(stmt.getMetaData(), tableEntry);

        } finally {
            db.returnConnection(connection);
        }
    }

    private void validateDirStructure() {

        fh.createDirectoriesIfNotExist(rootPath + tableSubPath);

        fh.createDirectoriesIfNotExist(rootPath + controllerSubPath);

    }

    private void createFiles(final TableMapEntry tableEntry, final ArrayList<DBTableEntry> tableStruct) {
        validateDirStructure();
        createEnumFile(tableEntry.tableName, tableStruct);
    }

    private void createFiles(ResultSetMetaData metaData, final TableMapEntry tableEntry) throws SQLException {

        ArrayList<DBTableEntry> tableStruct = new ArrayList<DBTableEntry>();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            try {

                TABLE_ENTRY_TYPE dbType = SQLTypeHelper.mapSQLType(metaData.getColumnType(i));
                tableStruct.add(new DBTableEntry(columnName, dbType));

            } catch (RuntimeException ex) {
                Logger.getLogger(TableGenerator.class.getName()).log(Level.SEVERE, ex.getMessage() + "for column: " + columnName, ex);
                System.exit(-1);
            }
        }

        createFiles(tableEntry, tableStruct);
    }

    private void createEnumFile(String tableName, ArrayList<DBTableEntry> tableStruct) {
        StringBuilder content = new StringBuilder();

        String className = getBasicClassName(tableName) + "Fields";

        content.append(getPackageContent(tablePackage, className))
                .append("\n\npublic enum ")
                .append(className)
                .append("{\n\n")
                .append(INDENTATION);
        boolean firstEntry = true;
        for (DBTableEntry dBTableEntry : tableStruct) {
            if (firstEntry) {
                firstEntry = false;
            } else {
                content.append(", ");
            }
            content.append(dBTableEntry.typeName);
        }
        content.append("\n}\n");
        try {
            fh.writeToFile(rootPath + tableSubPath + "/" + className + ".java", content.toString());
        } catch (IOException ex) {
            Logger.getLogger(TableGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static final String INDENTATION = "    ";

    private String getPackageContent(String TABLE_PACKAGE, String className) {
        return "\n\n\npackage " + TABLE_PACKAGE + "." + className + ";";
    }

    private String getBasicClassName(String tableName) {
        return tableName.toUpperCase();
    }

}
