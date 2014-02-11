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
import sit.db.datastructure.DataStructure;
import sit.db.table.SQLTypeHelper;
import sit.db.table.TABLE_ENTRY_TYPE;
import sit.io.FileHelper;

/**
 *
 * @author simon
 * @param <T>
 */
public class TableGenerator<T extends DataStructure<T> > {

    private final String rootDirForGenerateFiles;
    private final String rootPackage;
    private final String tableString = "table";
    private final String controllerString = "controller";
    private final String tableSubPath = "/" + tableString;
    private final String controllerSubPath = "/" + controllerString;
    private final String tablePackage;
    private final String controllerPackage;
    private final FileHelper fh = new FileHelper();
    private final String rootPath;

    public TableGenerator(String rootDirForGenerateFiles, String rootPackage, String rootPath) {
        this.rootDirForGenerateFiles = rootDirForGenerateFiles;
        this.rootPackage = rootPackage;
        this.rootPath = rootPath;
        this.tablePackage = rootPackage + "." + tableString;
        this.controllerPackage = rootPackage + "." + controllerString;

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
        createTableFile(tableEntry, tableStruct);
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

        String className = getFieldClassName(tableName);

        content.append(getPackageContent(tablePackage))
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
            content.append(dBTableEntry.dbTypeName);
        }
        content.append("\n}\n");
        try {
            fh.writeToFile(rootPath + tableSubPath + "/" + className + ".java", content.toString());
        } catch (IOException ex) {
            Logger.getLogger(TableGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getFieldClassName(String tableName) {
        String className = getBasicClassName(tableName) + "Fields";
        return className;
    }
    public static final String INDENTATION = "    ";

    private String getPackageContent(String TABLE_PACKAGE) {
        return "\n\n\npackage " + TABLE_PACKAGE + ";\n\n";
    }

    private String getBasicClassName(String tableName) {
        return tableName.toUpperCase();
    }

    private void createTableFile(TableMapEntry tableEntry, ArrayList<DBTableEntry> tableStruct) {
        StringBuilder content = new StringBuilder();

        String className = getBasicClassName(tableEntry.tableName) + "Table";
        String fieldClassName = getFieldClassName(tableEntry.tableName);

        content.append(getPackageContent(tablePackage))
                .append(getTableImports(tableEntry));

        content.append("\n\npublic class ")
                .append(className)
                .append(" extends Table<")
                .append(tableEntry.dataStructureName)
                .append(", ")
                .append(fieldClassName)
                .append("> {\n\n")
                .append(INDENTATION)
                .append("public ")
                .append(className)
                .append("() {\n")
                .append(INDENTATION).append(INDENTATION)
                .append("super(new StrictSITEnumMap(")
                .append(fieldClassName)
                .append(".class,\n").append(INDENTATION).append(INDENTATION).append(INDENTATION)
                .append("new TableEntry[]{\n\n");
        boolean firstEntry = true;
        for (DBTableEntry dBTableEntry : tableStruct) {
            if (firstEntry) {
                firstEntry = false;
            } else {
                content.append(", \n");
            }
            content.append(createTableTableEntry(fieldClassName, tableEntry, tableStruct, dBTableEntry));
        }
        content
                .append(" }));\n    }\n\n    @Override\n    public String getTableName() {\n        return \"")
                .append(tableEntry.tableName)
                .append("\";\n    }\n\n    @Override\n    protected ")
                .append(tableEntry.dataStructureName)
                .append(" createNewInstance() {\n        return new ")
                .append(tableEntry.dataStructureName)
                .append("();\n    }")
                .append("\n}\n");
        try {
            fh.writeToFile(rootPath + tableSubPath + "/" + className + ".java", content.toString());
        } catch (IOException ex) {
            Logger.getLogger(TableGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getTableImports(TableMapEntry tableEntry) {
        return TableStrings.imports + "import " + tableEntry.dataStructurePackage + "." + tableEntry.dataStructureName + ";\n\n";
    }

    private String createTableTableEntry(String fieldClassName, TableMapEntry tableEntry, ArrayList<DBTableEntry> tableStruct, DBTableEntry dBTableEntry) {
        StringBuilder result = new StringBuilder();
        result.append(INDENTATION + INDENTATION +"new TableEntry(")
                .append(fieldClassName)
                .append(".")
                .append(dBTableEntry.dbTypeName)
                .append(", \"")
                .append(dBTableEntry.name)
                .append("\", TABLE_ENTRY_TYPE.")
                .append(dBTableEntry.dbType.toString())
                .append(", new Mapper<")
                .append(tableEntry.dataStructureName)
                .append(">() {\n\n")
                .append(createGetter(fieldClassName, tableEntry, tableStruct, dBTableEntry))
                .append("\n\n")
                .append(createSetter(fieldClassName, tableEntry, tableStruct, dBTableEntry))
                .append("\n\n        })");

        return result.toString();
    }

    private String createGetter(String fieldClassName, TableMapEntry tableEntry, ArrayList<DBTableEntry> tableStruct, DBTableEntry dBTableEntry) {
        StringBuilder result = new StringBuilder();
        result.append(INDENTATION +INDENTATION + INDENTATION + "@Override\n")
                .append(INDENTATION +INDENTATION + INDENTATION + "public ")
                .append(dBTableEntry.javaTypeName)
                .append(" get").append(dBTableEntry.javaGetSetStub)
                .append("(").append(tableEntry.dataStructureName)
                .append(" dataStructureEntry) {\n                return dataStructureEntry.")
                .append(tableEntry.guessGetterForDBEntry(dBTableEntry.name))
                .append("();\n            }\n")
                ;

        return result.toString();
    }

    private String createSetter(String fieldClassName, TableMapEntry tableEntry, ArrayList<DBTableEntry> tableStruct, DBTableEntry dBTableEntry) {
        return "";
    }

   

    
    

}
