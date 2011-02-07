/*
 * XMLAccess.java
 *
 * Created on 31. Oktober 2004, 12:08
 */

package sit.tools.xml;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;


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
    
    public XMLLayerUtils readXMLInput(XMLLayerUtils startLevel, String nodeRoot, String xmlInput)
            throws SAXException{

        org.w3c.dom.Node aktNode = parseXML(xmlInput);
        
        while(aktNode != null){
  
            if (aktNode.getNodeName().equals(nodeRoot)){
                startLevel.readXMLInput(aktNode);
                break; //stop after the first found element
            }
            aktNode = aktNode.getNextSibling();
            
        }
     
        return startLevel;
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
