/*
 * XMLAccess.java
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 * Created on 31. Oktober 2004, 12:08
 */

package sit.xml;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;
import sit.io.FileHelper;


/**
 *
 * @author Simon Thiel
 */
public class XMLAccess {
    
    private static final XMLAccess singleton = new XMLAccess();
    
    
    private XMLAccess(){
    }
    
    public org.w3c.dom.Document parseXML(String xmlInput) throws SAXException{
     
        
        javax.xml.parsers.DocumentBuilderFactory factory
        = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document document = null;
        
        try {
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            java.io.InputStream inst =  new java.io.ByteArrayInputStream(xmlInput.getBytes());
            document = builder.parse(inst);

            
        }
        catch (javax.xml.parsers.ParserConfigurationException ex) {
            // Parser with specified options can't be built
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            
        }
        catch(java.io.IOException ex){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return document;
    }
    
    private org.w3c.dom.Node getNextRootNode(Stack<org.w3c.dom.Node> nodeStack){
        try{
            org.w3c.dom.Node curRootNode = nodeStack.peek().getFirstChild();
            if (curRootNode!=null){
                nodeStack.push(curRootNode);
                return curRootNode;
            }else{
                
                while (curRootNode==null){ //curRootNode == null because of else part
                    nodeStack.pop();
                    curRootNode = nodeStack.peek().getNextSibling();                   
                }
                nodeStack.push(curRootNode);
                return getNextRootNode(nodeStack);
            }
            
            }catch(EmptyStackException ex){

            }
        return null;
    }
    
    public org.w3c.dom.Node findFirstNodeWithTagname(org.w3c.dom.Node rootNode, String tagName){
        
        Stack<org.w3c.dom.Node> nodeStack = new Stack();
        nodeStack.push(rootNode);
        
        org.w3c.dom.Node curNode = rootNode;
        
        while(curNode != null){
  
            if (curNode.getNodeName().equals(tagName)){
                return curNode;
            }
            curNode = curNode.getNextSibling();
            if (curNode==null){ //try on next level
                curNode = getNextRootNode(nodeStack);
            }
        }
        return null;
    }
    
    public XMLLayerUtils readXMLInput(XMLLayerUtils xmlElement, String rootTag, String xmlInput)
            throws SAXException{

        org.w3c.dom.Node rootNode = parseXML(xmlInput);
        
        org.w3c.dom.Node startNode = findFirstNodeWithTagname(rootNode, rootTag);
        if (startNode!=null){
                xmlElement.readXMLInput(startNode);
        }
     
        return xmlElement;
    }
    
    public Element readXMLElementInputFromFile(String rootTag, String fileName) throws SAXException, FileNotFoundException, IOException {
        FileHelper fh = new FileHelper();
        return readXMLElementInput(rootTag, fh.readFromTextFile(fileName));
    }
    
     public Element readXMLElementInput(String rootTag, String xmlInput) throws SAXException{
        
        Element result = new Element(null,rootTag);
        return (Element) readXMLInput(result, rootTag, xmlInput);
    }
    
    public String toXML(XMLLayerUtils startLevel){
        StringBuilder result = new StringBuilder("<?xml version=\"1.0\"?>");
        result.append(startLevel.toXML());
        result.append("\n");
        return result.toString();
    }
    
 
    
    public static XMLAccess getInstance(){
        return singleton;
    }

    
    
}
