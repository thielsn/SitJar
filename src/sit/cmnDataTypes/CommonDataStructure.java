/*
 * CommonDataStructure.java
 *
 * Created on 30. Oktober 2004, 18:27
 */

package sit.cmnDataTypes;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Simon Thiel
 */
public class CommonDataStructure extends sit.tools.xml.XMLTool {
   
    
    /** Creates a new instance of CommonDataStructure */
    public CommonDataStructure() {
    }
    

 
    
    /**
     *
     * @param source
     * @param exp
     */    
    @Override
    protected void errorExit(String source, Exception exp){
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, 
                "Error in common data structure:"+source,exp);
        System.exit(-1);
        
    }
    
}
