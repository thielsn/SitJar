/*
 * XMLTool.java
 *
 * Created on 5. November 2004, 16:39
 */

package sit.tools.xml;
import sit.GlobalConsts;
/**
 *
 * @author Simon Thiel
 */
public class XMLTool {
    
    
    /**debughelper*/
    private sit.tools.DebugHelper dbg = sit.tools.DebugHelper.getInstance();
    
    
    /** Creates a new instance of XMLTool */
    public XMLTool() {
    }
    
    protected StringBuffer createLeadIn(String tagName){
        StringBuffer result = new StringBuffer();
        
        result.append("\n<");
        result.append(tagName);
        result.append(">");
        
        return result;
    }
    
    protected StringBuffer createLeadIn(String tagName, java.util.Vector xmlAttributeList){
        
        XMLAttribute attr;
        StringBuffer result = new StringBuffer();
        java.util.Iterator iter = xmlAttributeList.iterator();
        
        result.append("\n<");
        result.append(tagName);
        
        //add attributes
        while (iter.hasNext()){
            attr = (XMLAttribute)iter.next();
            result.append(" "+attr.toString());
        }
        
        result.append(">");
        
        return result;
    }
    protected StringBuffer createLeadIn(String tagName, XMLAttribute xmlAttribute){
        
        StringBuffer result = new StringBuffer();
        
        result.append("\n<");
        result.append(tagName);
        
        result.append(" "+xmlAttribute.toString());
        
        result.append(">");
        
        return result;
    }
    
    
    protected StringBuffer createLeadOut(String tagName){
        StringBuffer result = new StringBuffer();
        
        result.append("</");
        result.append(tagName);
        result.append(">");
        
        return result;
    }
    
    /**
     *  get all text of a element node
     **/
    protected String getText(org.w3c.dom.Node xmlInput){
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

    protected boolean getAttributeAsBool(org.w3c.dom.Node xmlInput, String tag){
        boolean result = false;
        String value = getAttribute(xmlInput, tag);
        if ((value!=null)&&(!value.equals(""))){


            try {
                result = Boolean.parseBoolean(value);
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }

        }else{
            System.err.println("Empty value or attribute attribute not found:"+tag+" returning false !");
        }
        
        return result;
    }

    protected int getAttributeAsInt(org.w3c.dom.Node xmlInput, String tag){
        int result = -1;
        String value = getAttribute(xmlInput, tag);
        if ((value!=null)&&(!value.equals(""))){

            try {
                result = Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
                System.err.println("Exception while parsing number for attribute:"+tag);
                numberFormatException.printStackTrace();
                result = -1;
            }
        }else{
            System.err.println("Empty value or attribute attribute not found:"+tag+" returning -1 !");
        }
        return result;
    }

    protected double getAttributeAsDouble(org.w3c.dom.Node xmlInput, String tag){
        double result = -1;
        String value = getAttribute(xmlInput, tag);
        if ((value!=null)&&(!value.equals(""))){

            try {
                result = Double.parseDouble(value);
            } catch (NumberFormatException numberFormatException) {
                System.err.println("Exception while parsing number for attribute:"+tag);
                numberFormatException.printStackTrace();
                result = -1;
            }
        }else{
            System.err.println("Empty value or attribute attribute not found:"+tag+" returning -1 !");
        }
        return result;
    }

    protected String getAttribute(org.w3c.dom.Node xmlInput, String tag){
        return ((org.w3c.dom.Element)xmlInput).getAttribute(tag);
    }
    
    protected java.util.Vector getAttributes(org.w3c.dom.Node xmlInput){
        java.util.Vector result = new java.util.Vector();
        org.w3c.dom.NamedNodeMap nodes = ((org.w3c.dom.Element)xmlInput).getAttributes();
        org.w3c.dom.Node aktNode;
        XMLAttribute aktAttr;
        
        for (int i=0;i<nodes.getLength();i++){
            aktNode = nodes.item(i);
            try{
                aktAttr = new XMLAttribute(aktNode.getNodeName(),aktNode.getNodeValue());
            }
            catch (org.w3c.dom.DOMException exp){
                exp.printStackTrace();
                System.err.println("DOMException - Attributevalue of "
                +aktNode.getNodeName()+" is set to Zero");
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
    protected String getTextNode(org.w3c.dom.Node xmlInput){
        String result = "";
        try{
            result=xmlInput.getNodeValue();
        }
        catch (org.w3c.dom.DOMException exp){
            exp.printStackTrace();
            System.err.println("DOMException - Textvalue of "
            +xmlInput.getNodeName()+" is set to Zero");
            
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
    public XMLAttribute getFirstAttributeByName(java.util.Vector attribs, String attribName) {
                
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
    public java.util.Vector getAttribsByName(java.util.Vector attribs,  String attribName){
        java.util.Iterator iter = attribs.iterator();
        java.util.Vector result = new java.util.Vector();
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
     * prints the message to system error
     * @param source
     * @param exp
     */
    protected void errorExit(String source, Exception exp){
        dbg.print("Error in " + source,GlobalConsts.DEBUG_CDS_ERROR);
        dbg.print("stacktrace:--------------------------",GlobalConsts.DEBUG_CDS_ERROR);
        exp.printStackTrace();
        System.exit(-1);
        
    }

    /**
     * returns the first child of the searched for tag
     * @param parent
     * @param tagname
     * @return
     */
    protected org.w3c.dom.Node getFirstChildWithTag( org.w3c.dom.Node parent, String tagname ){

        return getNextSiblingWithTag(parent.getFirstChild(), tagname);
    }

    /**
     * returns the next sibling with tag or null if not exists
     * @param sibling
     * @param tagname
     * @return
     */
    protected org.w3c.dom.Node getNextSiblingWithTag( org.w3c.dom.Node sibling, String tagname ){

        if (sibling==null){
            return null;
        }

        org.w3c.dom.Node nextSibling = sibling.getNextSibling() ;

        while(nextSibling != null){
            if (nextSibling.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE){
                if (nextSibling.getNodeName().equals(tagname)){
                     return nextSibling;
                }
            }
            nextSibling = nextSibling.getNextSibling();
        }
        return null;
    }
}
