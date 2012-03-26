/*
 * ProcessorAccess.java
 *
 * Created on 22. Februar 2006, 21:20
 *
 * @version $Revision: $
 *
 */

package sit.cmnDataTypes.interfaces;
import sit.cmnDataTypes.*;
/**
 *
 * @author  thiel
 */
public interface ProcessorAccess {
    
    /**
     *
     * @return
     */    
    public ElementProcessor getElementProcessor();
    /**
     *
     * @param eProc
     */    
    public void setElementProcessor(ElementProcessor eProc);
}
