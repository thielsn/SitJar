package sit.tools;
import java.io.*;

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
    public String readCompleteFile(String fileName){
        StatusString myStatus = new StatusString();
        String result = readCompleteFile(fileName,myStatus);
        System.out.println(myStatus.getStatus());
        return result;
    }
    
    /**
     * ganze datei als String einlesen
     * @param fileName fileName
     * @param myStatus StatusString, der nach Ausfuehrung den Status der Operation enthaelt.
     * @return Dateiinhalt
     */    
    public String readCompleteFile(String fileName, StatusString myStatus){
        //liest die angegebene Datei komplett in den String ein
        myStatus.addStatus("reading " + fileName);
        java.lang.StringBuffer alles=new StringBuffer("");
        try {
            String line;
            BufferedReader reader = new BufferedReader( new FileReader(fileName) );
            
            while ((line = reader.readLine()) != null) {
                //System.out.print("$$$$$$$$$$$$$$$$$$$$$$%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                //System.out.print(".");
                alles.append(line);
                alles.append(lineSep);
            }
            reader.close();
            myStatus.addStatus("got it.");
        }
        catch (Exception e2) {
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
    public boolean writeStringToFile(String fileName,String data,StatusString myStatus) {
        boolean result = false;
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(fileName));
            writer.write(data);
            writer.close();
            result = true;
        }
        catch (Exception ex) {
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
    public boolean appendStringToFile(String fileName,String data,StatusString myStatus) {
        boolean result = false;
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(fileName,true));
            writer.write(data);
            writer.close();
            result = true;
        }
        catch (Exception ex) {
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
    public boolean writeStringToFile(String fileName,String data) {
        boolean result;
        StatusString myStatus = new StatusString();
        
        result = writeStringToFile(fileName, data, myStatus);
        
        System.out.println(myStatus.getStatus());
        
        return result;
    }
    
    /**
     * fuehrt angegebenen befehl auf der system konsole aus
     * @param cmdline cmdline
     * @param myStatus myStatus
     */    
    public void CmdExec(String cmdline, StatusString myStatus) {
        
        myStatus.addStatus(cmdline);
        //System.out.println(eingabe);
        try {
            
            Process p = Runtime.getRuntime().exec(cmdline);
            
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        
    }
    /**
     * returns the directory of a complete filename
     * @param fileName complete filename
     * @return the directory
     */    
    public String pureDir(String fileName){
        //mit backslash
        int cj=0;
        for (int ci=0;ci<fileName.length();ci++ ) {
            if (fileName.charAt(ci)=='\\') {  cj=ci; }
        }
        if (cj>0) return fileName.substring(0,cj+1);
        else return "";
    }
    
    /**
     * returns the (short) filename of a complete filename
     * @param fileName complete filename
     * @return filename
     */    
    public String pureFile(String fileName){
        //gibt den kompletten dateinamen zur�ck aber ohne pfad
        String result="";
        for (int i = 0; i < fileName.length(); i++) {
            if (fileName.charAt(i)=='\\'){
                result="";
            }
            else {
                result=result+ fileName.charAt(i);
            }
        }
        return result;
    }


    public boolean fileExists(String filename){

        File f = new File(filename);
        return f.exists();


    }
    
}
