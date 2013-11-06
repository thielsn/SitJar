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
 *  Description of DataSet
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 26.03.2012
 */

package sit.parser;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public abstract class DataSet {

    protected boolean complete = false;

    public DataSet(){
        
    }

    /**
     * processes subsequent lines of a dataset
     * returns true in case the line still belongs to a dataset
     * in case the dataset was completed already it returns false
     * @param context 
     * @param line
     * @return true in case the line still belongs to a dataset; in case the dataset was completed already it returns false
     */
    public abstract boolean processLine(ParseContext context, String line);

    /**
     * indicates that this dataset was the last one found in the file
     * gives an opportunity to mark the dataset as completed which will
     * cause to have it added to the list of recognized datasets
     */
    public abstract void reachedEndOfFile();

    /**
     * @return the complete
     */
    public boolean isComplete() {
        return complete;
    }
}
