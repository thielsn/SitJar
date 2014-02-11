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
package sit.db.exception;

/**
 *
 * @author simon
 */
public class DBException extends Exception {

    private final String concernedTypeTag;
    private final String reason;
    private final int errorCode;

    public DBException(String concernedTypeTag, String reason, int errorCode) {
        super("Error when updating PARTICEPS database for " + concernedTypeTag + ": " + reason);
        this.concernedTypeTag = concernedTypeTag;
        this.reason = reason;
        this.errorCode = errorCode;
    }

    /**
     * @return the concernedTypeTag
     */
    public String getConcernedTypeTag() {
        return concernedTypeTag;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

}
