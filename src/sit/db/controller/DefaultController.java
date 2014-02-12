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
public class DefaultController<T extends DataStructure, TABLE_FIELDS extends Enum< TABLE_FIELDS>> extends Controller<T, TABLE_FIELDS> {

    public DefaultController(ConnectionManager db, Table<T, TABLE_FIELDS> table) {
        super(db, table);
    }

    @Override
    public T createEntry(T dataStructure) throws SQLException, DBException {
        return table.createEntry(db, dataStructure);
    }

    @Override
    public T updateEntry(T dataStructure) throws SQLException, DBException {
        return table.updateEntry(db, dataStructure);
    }

    @Override
    public T deleteEntry(T dataStructure)throws SQLException, DBException{
        if(table.deleteEntry(db, dataStructure)){
            return dataStructure;
        }else{
            return null;
        }

    }    

    @Override
    public List<T> getEntries(Map<TABLE_FIELDS, String> filter)throws SQLException, DBException{
        return table.getEntries(db, filter);
    }

    @Override
     public List<T> getAllEntries() throws SQLException, DBException {
         return table.getAllEntries(db);
     }

  

}
