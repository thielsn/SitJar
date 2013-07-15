/*
 * XMLTool.java
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 * Created on 5. November 2004, 16:39
 */

package sit.xml;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Node;

/**
 *
 * @author Simon Thiel
 */
public class XMLTool {
    
    
    
    
    /** Creates a new instance of XMLTool */
    public XMLTool() {
    }
    
    public StringBuffer createLeadIn(String tagName){
        StringBuffer result = new StringBuffer();
        
        result.append("\n<");
        result.append(tagName);
        result.append(">");
        
        return result;
    }
    
    public StringBuffer createLeadIn(String tagName, java.util.Vector<XMLAttribute> xmlAttributeList){
                
        StringBuffer result = new StringBuffer();
        
        
        result.append("\n<");
        result.append(tagName);
        
        //add attributes
        for (XMLAttribute attr : xmlAttributeList){
            result.append(" ").append(attr.toString());
        }
        
        result.append(">");
        
        return result;
    }
    public StringBuffer createLeadIn(String tagName, XMLAttribute xmlAttribute){
        
        StringBuffer result = new StringBuffer();
        
        result.append("\n<");
        result.append(tagName);
        
        result.append(" ").append(xmlAttribute.toString());
        
        result.append(">");
        
        return result;
    }
    
    
    public StringBuffer createLeadOut(String tagName){
        StringBuffer result = new StringBuffer();
        
        result.append("</");
        result.append(tagName);
        result.append(">");
        
        return result;
    }
    
    /**
     *  get all text of a element node
     *
     * @param xmlInput
     * @return  
     */
    public String getText(org.w3c.dom.Node xmlInput){
        if (xmlInput==null){
            return null;
        }
        String result = "";
        org.w3c.dom.Node childNode = null;
        
        childNode = xmlInput.getFirstChild();
        while(childNode != null){
            if (childNode.getNodeType()==org.w3c.dom.Node.TEXT_NODE){
                result += childNode.getNodeValue();
            }
            childNode = childNode.getNextSibling();
        }
        
        return result.trim();
    }
    
    
    public String getCData(org.w3c.dom.Node xmlInput){ 
        if (xmlInput==null){
            return null;
        }
        String result = "";
        org.w3c.dom.Node childNode = null;
        
        childNode = xmlInput.getFirstChild();
        while(childNode != null){
            if (childNode.getNodeType()==org.w3c.dom.Node.CDATA_SECTION_NODE){
                result += ((CDATASection)childNode).getData();
            }
            childNode = childNode.getNextSibling();
        }
        
        return result;
    }

    public boolean getAttributeAsBool(org.w3c.dom.Node xmlInput, String tag){
        boolean result = false;
        String value = getAttribute(xmlInput, tag);
        if ((value!=null)&&(!value.equals(""))){


            try {
                result = Boolean.parseBoolean(value);
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "", e);
                result = false;
            }

        }else{
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "Empty value or attribute attribute not found:"+tag+" returning false !");
        }
        
        return result;
    }

    public int getAttributeAsInt(org.w3c.dom.Node xmlInput, String tag){
        int result = -1;
        String value = getAttribute(xmlInput, tag);
        if ((value!=null)&&(!value.equals(""))){

            try {
                result = Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                        "Exception while parsing number for attribute:"+tag, ex);
                                result = -1;
            }
        }else{
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "Empty value or attribute attribute not found:"+tag+" returning -1 !");
        }
        return result;
    }

    public double getAttributeAsDouble(org.w3c.dom.Node xmlInput, String tag){
        double result = -1;
        String value = getAttribute(xmlInput, tag);
        if ((value!=null)&&(!value.equals(""))){

            try {
                result = Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                        "Exception while parsing number for attribute:"+tag, ex);
                result = -1;
            }
        }else{
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "Empty value or attribute attribute not found:"+tag+" returning -1 !");
        }
        return result;
    }

    public String getAttribute(org.w3c.dom.Node xmlInput, String tag){
        return ((org.w3c.dom.Element)xmlInput).getAttribute(tag);
    }
    
    public java.util.Vector<XMLAttribute> getAttributes(org.w3c.dom.Node xmlInput){
        java.util.Vector<XMLAttribute> result = new java.util.Vector();
        org.w3c.dom.NamedNodeMap nodes = ((org.w3c.dom.Element)xmlInput).getAttributes();
        org.w3c.dom.Node aktNode;
        XMLAttribute aktAttr;
        
        for (int i=0;i<nodes.getLength();i++){
            aktNode = nodes.item(i);
            try{
                aktAttr = new XMLAttribute(aktNode.getNodeName(),aktNode.getNodeValue());
            }
            catch (org.w3c.dom.DOMException exp){
                
                Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                        "DOMException - Attributevalue of "
                        +aktNode.getNodeName()+" is set to Zero", exp);
                aktAttr = new XMLAttribute(aktNode.getNodeName(),"");
            }
            result.add(aktAttr);
        }
        
        //((org.w3c.dom.Element)xmlInput).getAttribute(tag);
        
        return result;
    }

    /**
     *  Returns the Text of a Textnode
     **/
    public String getTextNode(org.w3c.dom.Node xmlInput){
        String result = "";
        try{
            result=xmlInput.getNodeValue();
        }
        catch (org.w3c.dom.DOMException exp){
           
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "DOMException - Textvalue of "
            +xmlInput.getNodeName()+" is set to Zero", exp);
            
        }
        return result;
    }
    
    /**
     * returns the first matching attribute of the given element
     * this is useful if there is only one attribte with the given name
     * which should be the normal case ;-)
     *
     * @param vector of XMLAttribute ..
     * @param attribName searched for attrib name
     * @return the attribute
     */
    public XMLAttribute getFirstAttributeByName(java.util.Vector<XMLAttribute> attribs, String attribName) {
                
        XMLAttribute aktAttrib = null;
        
        if (!attribs.isEmpty()){            
            
            java.util.Iterator iter = attribs.iterator();            
            while(iter.hasNext()){
                
                aktAttrib = (XMLAttribute)iter.next();
                if (aktAttrib.getTag().equals(attribName)){
                    return aktAttrib;
                }
            }
        }        
        return null;
    }
    
    /**
     * returns a vector of XMLAttributes with the same tagname
     * @param attribName the searchedfor attribute name
     * @return Vector of the found attributes with type XMLAttribute
     */
    public java.util.Vector<XMLAttribute> getAttribsByName(java.util.Vector<XMLAttribute> attribs,  String attribName){
        java.util.Iterator iter = attribs.iterator();
        java.util.Vector<XMLAttribute> result = new java.util.Vector();
        XMLAttribute aktAttrib = null;
        
        while(iter.hasNext()){
            aktAttrib = (XMLAttribute)iter.next();
            if (aktAttrib.getTag().equals(attribName)){
                result.add(aktAttrib);
            }
        }
        return result;
    }


    /**
     * returns the first child of the searched for tag
     * @param parent
     * @param tagname
     * @return
     */
    public org.w3c.dom.Node getFirstChildWithTag( org.w3c.dom.Node parent, String tagname ){
        Node firstChild = parent.getFirstChild();
        if (firstChild.getNodeName().equalsIgnoreCase(tagname)){
                return firstChild;
        }
        return getNextSiblingWithTag(firstChild, tagname);
    }

    /**
     * returns the next sibling with tag or null if not exists
     * @param sibling
     * @param tagname
     * @return
     */
    public org.w3c.dom.Node getNextSiblingWithTag( org.w3c.dom.Node sibling, String tagname ){

        if (sibling==null){
            return null;
        }

        org.w3c.dom.Node nextSibling = sibling.getNextSibling() ;

        while(nextSibling != null){
            if (nextSibling.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE){
                if (nextSibling.getNodeName().equalsIgnoreCase(tagname)){
                     return nextSibling;
                }
            }
            nextSibling = nextSibling.getNextSibling();
        }
        return null;
    }
    
    
}
