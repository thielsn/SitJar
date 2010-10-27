/*
 * DebugHelper.java
 *
 * Created on 2. November 2004, 09:48
 */

package sit.tools;
import sit.GlobalConsts;
/**
 *
 * @author Simon Thiel
 */
public class DebugHelper {
    
    /**
     *
     */    
    private static final DebugHelper singleton = new DebugHelper();
    
    /**
     *
     */    
    private java.io.PrintStream os = System.out;
    
    /**
     *
     */    
    private Tools myTools = new Tools();
    /**
     *
     */    
    private StatusString fileMessages = new StatusString(); 
    
    
    /**
     *
     */    
    private String logFile = GlobalConsts.SETTINGS_LOGS + "agent_log.txt";
    /**
     *
     */    
    private boolean printToFile = false;
    /**
     *
     */    
    private boolean firstTime = true;
    
    /** Creates a new instance of DebugHelper */
    private DebugHelper() {
    }
    
    /**
     *
     * @return
     */    
    public static DebugHelper getInstance(){
        return singleton;
    }
    
    /**
     *
     * @param text
     * @param debugLevel
     */    
    public synchronized void print(String text, int debugLevel){
        if (GlobalConsts.DEBUG_LEVEL>=debugLevel){
            if(printToFile){
                if (firstTime){
                    firstTime = false;
                    myTools.writeStringToFile(logFile, "AgentLog:\n",fileMessages);                    
                }
                myTools.appendStringToFile(logFile, text+"\n",fileMessages);
            }
            else{
                os.println(text);    
            }
            
        }
    }
    
    /**
     * print name value pair
     * @param name
     * @param value
     * @param debugLevel
     */    
    public void print(String name, String value, int debugLevel){
        this.print(name+": "+value,debugLevel);
    }
    
    /**
     *
     * @param name
     * @param value
     * @param debugLevel
     */    
    public void print(String name, int value, int debugLevel){
        this.print(name+": "+value,debugLevel);
    }
    
    /**
     * print Entering Function: +name
     * @param name
     * @param debugLevel
     */    
    public void printEF(String name, int debugLevel){
        this.print("Entering Function: "+name,debugLevel);
    }
    /**
     * print Exiting Function: +name
     * @param name
     * @param result
     * @param debugLevel
     */    
    public void printExF(String name,String result, int debugLevel){
        this.print("Exiting Function:"+name+" Result:"+result,debugLevel);
    }
    
    /**
     *
     * @param data
     * @param debugLevel
     * @param localFilename
     */    
    public void writeLogFile(String data, int debugLevel,String localFilename){
        if (GlobalConsts.DEBUG_LEVEL>=debugLevel){
            os.println("... writing DATA to logfile:"+localFilename);
            myTools.writeStringToFile(GlobalConsts.SETTINGS_LOGS+localFilename,data);
        }
    }
    
    
    /**
     * Getter for property os.
     * @return Value of property os.
     */
    public java.io.PrintStream getOs() {
        return os;
    }
    
    /**
     * Setter for property os.
     * @param os New value of property os.
     */
    public void setOs(java.io.PrintStream os) {
        this.os = os;
    }
    
    /**
     * Getter for property printToFile.
     * @return Value of property printToFile.
     */
    public boolean isPrintToFile() {
        return printToFile;
    }
    
    /**
     * Setter for property printToFile.
     * @param printToFile New value of property printToFile.
     */
    public void setPrintToFile(boolean printToFile) {
        this.printToFile = printToFile;
    }
    /**
     * returns true if the given debuglevel <= global debuglevel
     * @param debugLevel
     * @return
     */
    public boolean isDebug(int debugLevel){
        return (debugLevel<=GlobalConsts.DEBUG_LEVEL);
    }
}
