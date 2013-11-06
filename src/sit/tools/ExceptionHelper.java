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
 *  Description of ExceptionHelper
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @date 04.05.2012
 */
package sit.tools;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class ExceptionHelper {

    public static String stackTraceToString(Exception ex, String message) {
        StringBuilder errMessage = new StringBuilder();
        errMessage.append(message).append(": ").append(ex.getMessage()).append(":\n");

        for (StackTraceElement line : ex.getStackTrace()) {
            errMessage.append(line.toString()).append("\n");
        }
        return errMessage.toString();
    }
}
