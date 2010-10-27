/*
 * DefaultProps.java
 *
 * Created on 25. Maerz 2006, 20:53
 */

package sit.tools;

import java.io.*;
import java.util.Properties;

/**
 *
 * @author  thiel
 */
public class DefaultProps extends Properties{
    
    protected static String defaultHeader = null;
    protected String header = null;
    protected String fileName = "DefaultProperties.properties";
    protected String directory = "resources";
    
    /** Creates a new instance of DefaultProps */
    public DefaultProps() {
        header = defaultHeader;
        initDefaults();
    }
    
    protected void initDefaults(){
        //override to add default values
    }
    
    public String getPathFile(){
        return directory+System.getProperty("file.separator")+fileName;
    }
    
    
    
    /**
     * Reset properties with default values
     * @param properties New value of property properties.
     */
    public void resetDefaultProperties() {
        this.clear();
        this.putAll(defaults);
    }
    
    public boolean saveToFile(String fileName){
        boolean result = false;
        try {
            FileOutputStream propOutFile = new FileOutputStream( fileName );
            this.store(propOutFile, header);
            result = true;
        }
        
        catch ( IOException e ) {
            System.err.println( "I/O failed." );
        }
        return true;
    }
    
    public boolean saveToFile(){
        return saveToFile(getPathFile());
    }
    
    public void overwriteFileWithDefaults(String fileName){
        try {
            FileOutputStream propOutFile = new FileOutputStream( fileName );
            defaults.store(propOutFile, header);            
        }        
        catch ( IOException e ) {
            System.err.println( "I/O failed." );
        }
    }
    
    public void overwriteFileWithDefaults(){
        overwriteFileWithDefaults(getPathFile());
    }
    
    public boolean loadFromFile(String fileName){
        boolean result = false;
        try {
            System.out.println("read Properties: "+fileName);
            
            FileInputStream propInFile = new FileInputStream( fileName );
            
            this.clear();
            this.load( propInFile );
            result = true;
        }
        catch ( FileNotFoundException e ) {
            System.err.println( "Cannot find " + fileName );
        }
        catch ( IOException e ) {
            System.err.println( "I/O failed." );
        }
        return result;
    }
    
    public boolean loadFromFile(){
        
        return loadFromFile(getPathFile());
    }
    /**
     * Getter for property header.
     * @return Value of property header.
     */
    public java.lang.String getHeader() {
        return header;
    }
    
    /**
     * Setter for property header.
     * @param header New value of property header.
     */
    public void setHeader(java.lang.String header) {
        this.header = header;
    }
    
    /**
     * Getter for property fileName.
     * @return Value of property fileName.
     */
    public java.lang.String getFileName() {
        return fileName;
    }
    
    /**
     * Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Getter for property directory.
     * @return Value of property directory.
     */
    public java.lang.String getDirectory() {
        return directory;
    }
    
    /**
     * Setter for property directory.
     * @param directory New value of property directory.
     */
    public void setDirectory(java.lang.String directory) {
        this.directory = directory;
    }
    
    public boolean getBoolProperty(String key){
        if (this.getProperty(key)==null)
            return false;
        
        return this.getProperty(key).equalsIgnoreCase("true");
    }
    
    
    public int getIntProperty(String key){
        int result = -1;
        try{
            result = Integer.parseInt(this.getProperty(key));
        }
        catch(NumberFormatException exp){
            System.err.println("Key not found:"+key);
            exp.printStackTrace();
            throw new NullPointerException(exp.getMessage());
        }
        return result;
    }
    
    public int hashCode() {
        return header.hashCode();
    }
    
    public boolean equals(Object obj) {
        if (obj==null) return false;
        if (this==obj) return true;
        if (getClass() != obj.getClass()) return false;
        
        DefaultProps other = (DefaultProps) obj;
        return (other.hashCode()==hashCode());
    }
    
    public static String getDefaultHeader(){
        return defaultHeader;
    }
    
    public Object setProperty(String str, String str1) {
        Object retValue;
        System.out.println(fileName+":setProperty:"+str+"-->"+str1);
        retValue = super.setProperty(str, str1);
        return retValue;
    }
    
}
