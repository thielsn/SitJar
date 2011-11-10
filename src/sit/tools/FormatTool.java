/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class FormatTool {


    public static String getFormD(double d){
        return getFormD(d, 2);
    }

    public static String getFormD(double d, int numOfDecimalPlace){
        return getFormD(d, numOfDecimalPlace, '.');
    }



    public static String getFormD(double d, int numOfDecimalPlace, char decimalSeparator){
        DecimalFormatSymbols usedFormatSymbols =
        new DecimalFormatSymbols();
        usedFormatSymbols.setDecimalSeparator(decimalSeparator);

        StringBuilder pattern = new StringBuilder("#.");
        for (int i=0; i<numOfDecimalPlace; i++){
            pattern.append("#");
        }

        return (new DecimalFormat(pattern.toString(),usedFormatSymbols).format(d));
    }

}
