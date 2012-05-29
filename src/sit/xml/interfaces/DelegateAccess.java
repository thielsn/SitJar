/*
 * DelegateAccess.java
 *
 * Created on 22. Februar 2006, 21:20
 *
 * @version $Revision: $
 *
 */

package sit.xml.interfaces;

import sit.xml.ElementDelegate;

/**
 *
 * @author  thiel
 */
public interface DelegateAccess {
    
    /**
     *
     * @return
     */    
    public ElementDelegate getElementProcessor();
    /**
     *
     * @param eProc
     */    
    public void setElementProcessor(ElementDelegate eProc);
}
