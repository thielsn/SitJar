/*
 *  Description of CommandLineHelper
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
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
     */
    public Process execCmd(String cmdline) throws IOException {
        return Runtime.getRuntime().exec(cmdline);
    }
}
