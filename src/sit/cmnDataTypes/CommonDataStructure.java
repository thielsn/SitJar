/*
 * CommonDataStructure.java
 *
 * Created on 30. Oktober 2004, 18:27
 */

package sit.cmnDataTypes;
import sit.GlobalConsts;

/**
 *
 * @author Simon Thiel
 */
public class CommonDataStructure extends sit.tools.xml.XMLTool {
    /**debughelper*/
    protected sit.tools.DebugHelper dbg
    = sit.tools.DebugHelper.getInstance();
    
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
        dbg.print("Error in " + source,GlobalConsts.DEBUG_CDS_ERROR);
        dbg.print("stacktrace:--------------------------",GlobalConsts.DEBUG_CDS_ERROR);
        exp.printStackTrace();
        System.exit(-1);
        
    }
    
}
