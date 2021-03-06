/*
* Copyright 2013 Simon Thiel
*
* This file is part of SitJar.
*
* SitJar is free software: you can redistribute it and/or modify
* it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SitJar is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.
*/

/*
 * Element.java
 *
 * Created on 14. Februar 2006, 13:59
 *
 */

package sit.xml;


/**
 * The basic data structure class. Used to handle all sort of xml-like data.
 * @author thiel
 */
public class Element extends CommonDataStructure implements XMLLayerUtils{
    
    /**
     * The attributes of the element.
     */    
    protected java.util.Vector attributes; // of type sit.xml.XMLAttribute
    /**
     * The sub elements of the element.
     */    
    protected java.util.Vector elements; //of type Element
    /**
     * The root of the data structure. May point to it self if is root.
     */    
    protected Element rootNode = null;
    
    /**
     * Name of the element.
     */    
    protected String tagName = "NoTag";
    /**
     * Textual content.
     */    
    protected String text = "";
    
    
    /**
     * Creates a new instance of Element
     * @param rootNode the root Element of the structure.
     * May be null if the element is the root itself.
     * @param tagName Element name
     */
    public Element(Element rootNode,String tagName) {
        elements = new java.util.Vector();
        attributes = new java.util.Vector();
        this.tagName = tagName;
        if (rootNode==null){
            rootNode=this;
        }
        this.rootNode = rootNode;
    }
    
    /**
     * Load Element data from xml node.
     * @param xmlInput xml node
     */
    public void readXMLInput(org.w3c.dom.Node xmlInput) {
        org.w3c.dom.Node childNode = null;
        Element newElement;
        
        //init
        text="";
        elements = new java.util.Vector();
       
        //read attributes
        attributes = getAttributes(xmlInput);
        
        
        //read subelements
        childNode = xmlInput.getFirstChild();
        while(childNode != null){
            //read subelements
            if (childNode.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE){
                newElement = new Element(rootNode, childNode.getNodeName());
                elements.add(newElement);
                newElement.readXMLInput(childNode);
            }
            //read text
            else if (childNode.getNodeType()==org.w3c.dom.Node.TEXT_NODE){
                text=text+getTextNode(childNode);
            }
            childNode = childNode.getNextSibling();
        }
    }
    
    /**
     * Recursively printing the Element content as xml String.
     * @return xml text
     */
    public String toXML() {
        

        
        StringBuilder result = new StringBuilder("\t");
        java.util.Iterator iter;
        Element aktElement;
        //lead in
        result.append(createLeadIn(tagName, attributes));
        
        //all subelements
        iter = elements.iterator();
        while(iter.hasNext()){
            aktElement = ((Element)iter.next());
            result.append(aktElement.toXML());
        }
        
        //add text
        result.append(this.text);
        
        //lead out
        result.append(createLeadOut(tagName));
        //result.append("\n");
        return result.toString();
    }
    
    
    
    /**
     * Getter for property tagName.
     * @return Value of property tagName.
     */
    public java.lang.String getTagName() {
        return tagName;
    }
    
    
    /**
     * Getter for property attributes.
     * @return Value of property attributes.
     */
    public java.util.Vector getAttributes() {
        return attributes;
    }
    
    /**
     * Setter for property attributes.
     * @param attributes New value of property attributes.
     */
    public void setAttributes(java.util.Vector attributes) {
        this.attributes = attributes;
    }
    
    /**
     * Getter for property elements.
     * @return Value of property elements.
     */
    public java.util.Vector getElements() {
        return elements;
    }
    
    /**
     * Setter for property elements.
     * @param elements New value of property elements.
     */
    public void setElements(java.util.Vector elements) {
        this.elements = elements;
    }
    
    /**
     * add attribute to attributes
     * @param newAttrib the new attribute
     */
    public void addAttribute(XMLAttribute newAttrib){
        if (newAttrib==null)throw new NullPointerException("Attribute is null");
        attributes.add(newAttrib);
    }
    
    /**
     * add element to elements
     * @param newElement the new Element
     */
    public void addElement(Element newElement){
        if (newElement==null)throw new NullPointerException("Element is null");
        elements.add(newElement);
    }
    
    /**
     * returns the root element of the data structure
     * @return Value of property rootNode.
     */
    public Element getRootNode() {
        return rootNode;
    }
    
    /**
     * Setter for property rootNode.
     * @param rootNode New value of property rootNode.
     */
    public void setRootNode(Element rootNode) {
        this.rootNode = rootNode;
    }
    
    /**
     * returns a vector of (child)Elements with the same tagname.
     * @param childName the searched for element name
     * @return Vector of found Elements of type Element
     */
    public java.util.Vector getChildsByName(String childName){
        java.util.Iterator iter = elements.iterator();
        java.util.Vector result = new java.util.Vector();
        Element aktElement = null;
        
        while(iter.hasNext()){
            aktElement = (Element)iter.next();
            if (aktElement.getTagName().equals(childName)){
                result.add(aktElement);
            }
        }
        return result;
    }
    
    /**
     * returns a vector of XMLAttributes with the same tagname
     * @param attribName the searched for attribute name
     * @return Vector of the found attributes with type XMLAttribute
     */
    public java.util.Vector getAttribsByName(String attribName){     
        return getAttribsByName(attributes,attribName);
    }
    
    /**
     * returns the value of the first XMLAttribute with the specified tagname if available null otherwise
     * @param attribName the searched for attribute name
     * @return value of first fitting XMLAttribute or null if not found
     */
    public String getFirstAttributeValueByName(String attribName){
        java.util.Vector<XMLAttribute> result = getAttribsByName(attributes,attribName);
        if (result.isEmpty()){
            return null;
        }
        return result.get(0).getValue();
    }
    
    
    
    /**
     * returns the text content
     * @return Value of property text.
     */
    public java.lang.String getText() {
        return text;
    }
    
    /**
     * Setter for property text.
     * @param text New value of property text.
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }
    
    /**
     * Setter for property text.
     * @param text New value of property text.
     */
    public void addText(java.lang.String text) {
        this.text += text;
    }
    

}
