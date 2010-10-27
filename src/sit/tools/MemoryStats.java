/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
