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
package sit.db.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import sit.db.ConnectionManager;
import sit.db.datastructure.DataStructure;
import sit.db.exception.DBException;
import sit.db.table.Table;

/**
 *
 * @author simon
 * @param <T>
 * @param <TABLE_FIELDS>
 */
public abstract class Controller<T extends DataStructure, TABLE_FIELDS extends Enum<TABLE_FIELDS>> {

    protected final ConnectionManager db;
    protected final Table<T, TABLE_FIELDS> table;

    public Controller(ConnectionManager db, Table<T, TABLE_FIELDS> table) {
        this.db = db;
        this.table = table;
    }

    public abstract T createEntry(T dataStructure) throws SQLException, DBException;

    public abstract T deleteEntry(T dataStructure) throws SQLException, DBException;

    public abstract List<T> getAllEntries() throws SQLException, DBException;

    public abstract List<T> getEntries(Map<TABLE_FIELDS, String> filter) throws SQLException, DBException;

    public abstract T updateEntry(T dataStructure) throws SQLException, DBException;

}
