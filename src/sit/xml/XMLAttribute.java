/*
 * XMLAttribute.java
 * Version 1.01
 * Created on 3. November 2004, 14:30
 * Modified 11. April 2006
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */

package sit.xml;

/**
 *
 * @author Simon Thiel
 */
public class XMLAttribute {
    
    private String tag;
    private String value;
    
    /** Creates a new instance of XMLAttribute */
    public XMLAttribute(String tag, String value) {
        this.tag = tag;
        this.value = value;
    }
    
    /**
     * Getter for property tag.
     * @return Value of property tag.
     */
    public java.lang.String getTag() {
        return tag;
    }
    
    /**
     * Setter for property tag.
     * @param tag New value of property tag.
     */
    public void setTag(java.lang.String tag) {
        this.tag = tag;
    }
    
    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public java.lang.String getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }
    
    public String toString(){
        if (tag==null) 
            throw new NullPointerException("Attribute:name==null\n");
            
        if (value==null) 
            return tag + "=\"\"";        
        
        return tag + "=\""+value.trim()+"\"";
    }
    

    
}
