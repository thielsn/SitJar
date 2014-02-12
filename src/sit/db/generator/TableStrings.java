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

package sit.db.generator;

/**
 *
 * @author simon
 */
public class TableStrings {
    static final String header = "/**\n *\n * Generated by sit.db.generator.TableGenerator\n *\n */";
    static final String imports = "import sit.db.table.TABLE_ENTRY_TYPE;\nimport sit.db.table.Mapper;\nimport sit.db.table.TableEntry;\nimport sit.db.table.Table;\nimport java.sql.SQLException;\nimport java.sql.Timestamp;\nimport java.sql.Date;\nimport sit.db.exception.DBException;\nimport sit.sstl.StrictSITEnumMap;\n\n";
    
}
