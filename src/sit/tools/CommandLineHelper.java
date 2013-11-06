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
 *  Description of CommandLineHelper
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @date 30.04.2012
 */
package sit.tools;

import java.io.IOException;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class CommandLineHelper {

    public String generateCmdLine(String[] argv) {

        String sysString = System.getProperty("os.name");
        String result = "";
        for (String param : argv) {
            result += param + " ";
        }

        if (sysString.startsWith("Windows 9")) {
            result = "start COMMAND.COM /c " + result;

        } else if ((sysString.startsWith("W")) || (sysString.startsWith("w"))) { //nt 2000 millenium XP
            result = "start cmd /c " + result;

        }
        return result;

    }

    /**
     * executes commandline at console
     *
     * @param cmdline cmdline
     * @return
     * @throws java.io.IOException
     */
    public Process execCmd(String cmdline) throws IOException {
        return Runtime.getRuntime().exec(cmdline);
    }
}
