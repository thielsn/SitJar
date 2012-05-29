/*
 * ElementDelegate.java
 *
 * Created on 22. Februar 2006, 21:22
 *
 * @version $Revision: $
 */

package sit.xml;

/**
 *
 * @author  thiel
 */
public abstract class ElementDelegate {
    
    protected Element myElement = null; //the Element to which the writer is added
    
    /**
     * Creates a new instance of ElementDelegate
     * @param myElement
     */
    public ElementDelegate(Element myElement ) {
        this.myElement = myElement;
    }
    
    /**
     *
     * @return
     */    
    public abstract Object doProcessing();
}
