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
 *  Description of FileHelperI
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @date 26.03.2012
 */

package sit.io;

import java.io.IOException;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public interface FileHelperI {
    public void writeToFile(String fileName, String content) throws IOException;
}
