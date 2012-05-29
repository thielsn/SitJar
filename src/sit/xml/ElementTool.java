/*
 * Collection of tools to search and process Element datastructures
 * ElementTool.java
 *
 * Created on 17. Februar 2006, 09:58
 *
 * @version $Revision: $
 */

package sit.xml;
/**
 *
 * @author  thiel
 */
public class ElementTool {
    
    
    
    /** Creates a new instance of ElementTool */
    public ElementTool() {
    }

    public Element getFirstChildElementByName(Element start,String name ){
        Element result = null;
        java.util.Vector elems = start.getChildsByName(name);
        if (!elems.isEmpty()){
            result = (Element)elems.get(0);
        }
        return result;
    }
    
    /**
     * returns the _first_ element that is described by a path of elementnames stored in
     * a vector
     * Example:
     * Meta
     * Sender
     * Enterprise
     * ....
     *
     * it returns null if tha path cannot be found
     * @param start start Element
     * @param path the described path as vector
     * @return first found element
     */
    public Element getElementByPath(Element start, java.util.Vector path){
        Element result = null;
        java.util.Iterator iter2;
        Element aktElement;
        java.util.Vector hypos = new java.util.Vector();
        java.util.Vector hypos_new;
        
        hypos.add(start);
        
        String aktTagName;
        
        java.util.Iterator iter = path.iterator();
        while(iter.hasNext()){
            aktTagName = (String)(iter.next());
            iter2 = hypos.iterator();
            hypos_new = new java.util.Vector(); //new vector for new layer
            while (iter2.hasNext()){
                aktElement = (Element)iter2.next();
                hypos_new.addAll((java.util.Collection)(aktElement.getChildsByName(aktTagName)));
            }
            if (hypos_new.isEmpty()){
                return null; //break if the searched element wasn't found
            }
            hypos = hypos_new;
            
        }
        if (!hypos.isEmpty()){
            result = (Element)hypos.get(0);
        }
        return result;
    }
    
    /**
     * returns the _first_  element with given tagname starting in the datastructure
     * with startElement
     * @param startElement start
     * @param searchedTagName searched for tagname
     * @return the first Element or null
     */
    public Element findElementByTagName(Element startElement, String searchedTagName){
        
        Element result = null;
        Element aktNode = null;
        
        String aktNodeName;
        java.util.Iterator iter;
        java.util.Stack nodeStack = new java.util.Stack();
        
        nodeStack.push(startElement);//pushRootElement
        while((!nodeStack.isEmpty()) && (result==null)){
            
            aktNode = (Element)nodeStack.pop();//start with last child
            
            iter = aktNode.getElements().iterator();
            while(iter.hasNext()){//push all childs
                nodeStack.push(iter.next());
            }
            
            aktNodeName = aktNode.getTagName(); // get tagName
            
            //check if it is the searched one
            if ((aktNodeName!=null)&& (aktNodeName.equals(searchedTagName))){
                result=aktNode; //element found
                //System.out.println("Element found:"+result.getTagName());
            }
        }
        return result;
    }
    
    /**
     * returns the _first_  element with given tagname starting in the datastructure
     * with startElement
     * prefixes get ignored
     * @param startElement start
     * @param searchedTagName searched for tagname
     * @return the first Element or null
     */
    public Element findElementByTagNameIgnorePrefix(Element startElement, String searchedTagName){
        
        Element result = null;
        Element aktNode = null;
        String searchedNWithoutPrefix = getLocalPartOfNodeName(searchedTagName);
        
        String aktNodeName;
        java.util.Iterator iter;
        java.util.Stack nodeStack = new java.util.Stack();
        
        nodeStack.push(startElement);//pushRootElement
        while((!nodeStack.isEmpty()) && (result==null)){
            
            aktNode = (Element)nodeStack.pop();//start with last child
            
            iter = aktNode.getElements().iterator();
            while(iter.hasNext()){//push all childs
                nodeStack.push(iter.next());
            }
            
            aktNodeName = aktNode.getTagName(); // get tagName
            
            //check if it is the searched one
            if ((aktNodeName!=null)&& 
            (getLocalPartOfNodeName(aktNodeName).equals(searchedNWithoutPrefix))){
                result=aktNode; //element found
                //System.out.println("Element found:"+result.getTagName());
            }
        }
        return result;
    }
    
    /**
     * strips the prefix from element names
     *
     * exmpl:
     * urn:name --> name
     * @param nodeName ...
     * @return the pure name
     */
    private String getLocalPartOfNodeName(String nodeName){
        java.util.StringTokenizer stz = new java.util.StringTokenizer(nodeName,":");
        String result = null;
        
        while (stz.hasMoreTokens()){
            result = stz.nextToken();
        }
        return result;
    }
    /**
     * returns all elements with given tagname starting in the datastructure
     * with startElement
     * @param startElement start
     * @param searchedTagName searched for tagname
     * @return found elements or empty Vector
     */
    public java.util.Vector findElementsByTagName(Element startElement, String searchedTagName){
        
        java.util.Vector result = new java.util.Vector();
        Element aktNode = null;
        
        String aktNodeName;
        java.util.Iterator iter;
        java.util.Stack nodeStack = new java.util.Stack();
        
        nodeStack.push(startElement);//pushRootElement
        while((!nodeStack.isEmpty())){
            
            aktNode = (Element)nodeStack.pop();//start with last child
            
            iter = aktNode.getElements().iterator();
            while(iter.hasNext()){//push all childs
                nodeStack.push(iter.next());
            }
            aktNodeName = aktNode.getTagName(); // get tagName
            
            //check if it is the searched one
            if ((aktNodeName!=null)&& (aktNodeName.equals(searchedTagName))){
                result.add(aktNode); //element found

            }
        }
        return result;
    }
    
    
    /**
     * Sets the value to the first matching attribute of the given element.
     * This is useful, if there is only one attribte with the given name,
     * which should be the normal case ;-)
     *
     * FIXME move this code to the element ???
     * @param aktElement ..
     * @param attribName name of the attribute
     * @param value the new value
     * @return true if attribute have been found
     */
    public boolean setValueToAttribByName(Element aktElement, String attribName, String value) {
        XMLAttribute aktAttrib = getFirstAttributeByName(aktElement, attribName);
        
        if (aktAttrib==null){ //attrib could no be found
            return false;
        }
        aktAttrib.setValue(value);
        return true;
        
    }
    
    /**
     * returns the first matching attribute of the given element
     * this is useful if there is only one attribte with the given name
     * which should be the normal case ;-)
     *
     * @param aktElement ..
     * @param attribName searched for attrib name
     * @return the attribute
     */
    public XMLAttribute getFirstAttributeByName(Element aktElement, String attribName) {
        return aktElement.getFirstAttributeByName(aktElement.getAttributes(),attribName);
    }
    

}