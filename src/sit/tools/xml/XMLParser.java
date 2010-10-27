/*
 * XMLParser.java
 *
 * Created on 17. Februar 2006, 11:57
 */

package sit.tools.xml;
import org.w3c.dom.Document;
import sit.cmnDataTypes.Element;
/**
 *
 * @author  thiel
 */
public class XMLParser {
    
    /** Creates a new instance of XMLParser */
    public XMLParser() {
    }
    
   public String documentToString(org.w3c.dom.Document xmlDoc){
        Element newElement;
        Element rootNode =  new Element(null,"root");
        
        
        org.w3c.dom.Node aktNode = xmlDoc.getFirstChild();
        
        while(aktNode != null){
            
            newElement = new Element(rootNode, aktNode.getNodeName());
            rootNode.addElement(newElement);
            newElement.readXMLInput(aktNode);
            
            aktNode = aktNode.getNextSibling();
        }
        
        
        StringBuffer xmlOut = new StringBuffer();
        Element aktElement;
        
        java.util.Iterator iter = rootNode.getElements().iterator();
        while(iter.hasNext()){
            aktElement = (Element)iter.next();
            xmlOut.append(aktElement.toXML());
            
        }
        return xmlOut.toString();
   }
}
