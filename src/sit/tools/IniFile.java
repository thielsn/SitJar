/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.tools;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class IniFile {

    private class Section {

        private Hashtable<String, String> entries = new Hashtable();

        public void clear() {
            entries.clear();
        }

        public void set(String name, String value) {
            entries.put(name, value);
        }

        public String get(String name) {
            return entries.get(name);
        }

        private Iterable<Entry<String, String>> getEntries() {
            return entries.entrySet();
        }
    };
    private String fileName = null;
    private Hashtable<String, Section> sections = new Hashtable();

    public IniFile(String fileName) {
        this.fileName = fileName;
    }

    public void loadIni() throws IOException {
        sections.clear();
        
        try {
            BufferedReader myReader = new BufferedReader(new FileReader(fileName));
            Section currentSection = null;
            String line = null;

            while ((line = myReader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty() || line.startsWith(";")) {
                    continue;
                }                
                

                if (line.startsWith("[")) {    // found section
                    try {
                        String sectionName = line.substring(line.indexOf("[") + 1, line.indexOf("]"));

                        if (sections.contains(sectionName)){
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "found several sections with same name:"
                                    + sectionName + " - ignoring all, but the first" );
                            currentSection=null;
                            continue;
                        }

                        currentSection = new Section();
                        sections.put(sectionName, currentSection);

                    } catch (IndexOutOfBoundsException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, line, ex);
                    }


                } else {    // name-value pair

                    if (currentSection==null){
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Found entries with no section "
                                + line
                                +" - ignoring");
                        continue;
                    }

                    String[] nameValue = line.split("=");
                   // System.out.println(nameValue.toString());
                    String value = "";
                    if (nameValue.length > 1) {
                        value = nameValue[1];
                    }
                    if (nameValue.length > 0) {
                        currentSection.set(nameValue[0], value);
                    }

                }
                               
            }
            myReader.close();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "filename:"+fileName, ex);
            throw ex;
        }
    }

    public boolean getBoolValue(String sectionName, String name, boolean defaultValue) {
        String value = getValue(sectionName, name);
        if (value == null) {
            return defaultValue;
        }// else
        return toBoolean(value);
    }

    public String getStringValue(String sectionName, String name, String defaultValue) {
        String value = getValue(sectionName, name);
        if (value == null) {
            return defaultValue;
        }// else
        return value;

    }

    private String getValue(String sectionName, String name) {
        Section section = sections.get(sectionName);
        if (section!=null){
            return section.get(name);
        }
        return null;
    }

    private boolean toBoolean(String value) {
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public int getIntValue(String section, String name, int defaultValue) {
        String value = getValue(section, name);
        if (value == null) {
            return defaultValue;
        }// else
        return toInteger(value, defaultValue);
    }

    private int toInteger(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            //in this case we return default value
        }
        return defaultValue;
    }

    public void setValue(String sectionName, String name, String value) {
        Section section = sections.get(sectionName);
        if (section==null){
            section = new Section();
            sections.put(sectionName, section);
        }
        section.set(name, value);
    }

    public void setValue(String section, String name, int value) {
        setValue(section, name, "" + value);
    }

    public void setValue(String section, String name, long value) {
        setValue(section, name, "" + value);
    }

    public void setValue(String section, String name, boolean value) {
        setValue(section, name, "" + value);
    }

    public void saveIni() throws IOException {
        try {
	    PrintStream f = new PrintStream(new FileOutputStream(fileName));
            f.print("; Inifile:\n;\n;\n;\n;\n");
            
            for (Entry<String,Section> section : sections.entrySet()) {
                
                f.print("["+section.getKey()+"]\n");
                for (Entry<String, String> entry : section.getValue().getEntries()){
                    f.print(entry.getKey()+"="+entry.getValue()+"\n");
                }
            }

    	    f.close();
    	}catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "filename:"+fileName, ex);
            throw ex;
        }
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (Entry<String,Section> section : sections.entrySet()) {

                result.append("[")
                        .append(section.getKey())
                        .append("]\n");

                for (Entry<String, String> entry : section.getValue().getEntries()){
                    result.append(entry.getKey())
                            .append("=")
                            .append(entry.getValue())
                            .append("\n");
                }
            }
        return result.toString();
    }
}
