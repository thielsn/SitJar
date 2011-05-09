package sit.tools;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * Beschreibung: Tools zur einfachen Handhabung von Dateien und Strings
 * Copyright:     Copyright (c) 2001
 * Organisation: 
 * @author Simon Thiel
 * @version 1.0
 */
public class Tools {

    /**
     * System abhaengiger lineseparator wird im constructor auf
     * System.getProperty("line.separator") gesetzt
     */
    public String lineSep;

    /**
     * constructor
     */
    public Tools() {
        lineSep = System.getProperty("line.separator");
    }

    /**
     * ganze datei als String einlesen
     * @param fileName fileName
     * @return Dateiinhalt
     */
    public String readCompleteFile(String fileName) {
        StatusString myStatus = new StatusString();
        String result = readCompleteFile(fileName, myStatus);
        System.out.println(myStatus.getStatus());
        return result;
    }

    /**
     * ganze datei als String einlesen
     * @param fileName fileName
     * @param myStatus StatusString, der nach Ausfuehrung den Status der Operation enthaelt.
     * @return Dateiinhalt
     */
    public String readCompleteFile(String fileName, StatusString myStatus) {
        //liest die angegebene Datei komplett in den String ein
        myStatus.addStatus("reading " + fileName);
        java.lang.StringBuffer alles = new StringBuffer("");
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            while ((line = reader.readLine()) != null) {
                //System.out.print("$$$$$$$$$$$$$$$$$$$$$$%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                //System.out.print(".");
                alles.append(line);
                alles.append(lineSep);
            }
            reader.close();
            myStatus.addStatus("got it.");
        } catch (Exception e2) {
            myStatus.addStatus("cannot read " + fileName);
            alles = new StringBuffer("cannot read " + fileName);
        }

        return alles.toString();
    }

    /**
     * schreibt data in angegebene Datei
     * ueberschreibt existierende dateien
     * @param fileName fileName
     * @param data data
     * @param myStatus Status
     * @return true wenn erfolgreich
     */
    public boolean writeStringToFile(String fileName, String data, StatusString myStatus) {
        boolean result = false;
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(fileName));
            writer.write(data);
            writer.close();
            result = true;
        } catch (Exception ex) {
            myStatus.addStatus("Error: " + ex.toString());
        }
        return result;
    }

    /**
     * schreibt data in angegebene Datei
     * haengt an existierende Dateien an
     * @param fileName fileName
     * @param data data
     * @param myStatus Status
     * @return true wenn erfolgreich
     */
    public boolean appendStringToFile(String fileName, String data, StatusString myStatus) {
        boolean result = false;
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(fileName, true));
            writer.write(data);
            writer.close();
            result = true;
        } catch (Exception ex) {
            myStatus.addStatus("Error: " + ex.toString());
        }
        return result;
    }

    /**
     * schreibt data in angegebene Datei
     * ueberschreibt existierende dateien
     * @param fileName fileName
     * @param data data
     * @return true wenn erfolgreich
     */
    public boolean writeStringToFile(String fileName, String data) {
        boolean result;
        StatusString myStatus = new StatusString();

        result = writeStringToFile(fileName, data, myStatus);

        System.out.println(myStatus.getStatus());

        return result;
    }

    public String generateCmdLine(String[] argv) {

        String sysString = System.getProperty("os.name");
        String result = "";
        for (String param : argv){
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
     * @param cmdline cmdline
     * @param myStatus myStatus
     */
    public void execCmd(String cmdline, StatusString myStatus) {

        myStatus.addStatus(cmdline);

        try {

            Process p = Runtime.getRuntime().exec(cmdline);

        } catch (Exception err) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, err);

        }

    }

    /**
     * returns the directory of a complete filename
     * @param fileName complete filename
     * @return the directory
     */
    public String pureDir(String fileName) {

        return (new File(fileName)).getPath();
    }

    /**
     * returns the (short) filename of a complete filename
     * @param fileName complete filename
     * @return filename
     */
    public String pureFile(String fileName) {
        return (new File(fileName)).getName();
    }

    public boolean fileExists(String filename) {

        File f = new File(filename);
        return f.exists();


    }
}
