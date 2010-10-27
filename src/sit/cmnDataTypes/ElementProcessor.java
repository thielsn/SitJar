/*
 * ElementProcessor.java
 *
 * Created on 22. Februar 2006, 21:22
 */

package sit.cmnDataTypes;

/**
 *
 * @author  thiel
 */
public abstract class ElementProcessor {
    
    protected Element myElement = null; //the Element to which the writer is added
    
    /**
     * Creates a new instance of ElementProcessor
     * @param myElement
     */
    public ElementProcessor(Element myElement ) {
        this.myElement = myElement;
    }
    
    /**
     *
     * @return
     */    
    public abstract Object doProcessing();
}
