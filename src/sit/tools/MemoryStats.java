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
 *  @author Simon Thiel <simon.thiel at gmx.de>
 */
package sit.tools;

import java.text.DecimalFormat;

/**
 *
 * @author thiel
 */
public class MemoryStats {

    private static String getFormatFloat(double f) {
        return (new DecimalFormat("#.####").format(f));
    }

    public static String printMemoryStats() {
        String result = "";
        int mb = 1024 * 1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();


        //Print used memory
        result += "Used Memory:"
                + getFormatFloat((runtime.totalMemory() - runtime.freeMemory()) / mb);

        //Print free memory
        result += " Free Memory:"
                + getFormatFloat(runtime.freeMemory() / mb);

        //Print total available memory
        result += " Total Memory:" + getFormatFloat(runtime.totalMemory() / mb);

        //Print Maximum available memory
        result += " Max Memory:" + getFormatFloat(runtime.maxMemory() / mb);
        return result;
    }
}
