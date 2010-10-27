/*
 * XMLAccess.java
 *
 * Created on 31. Oktober 2004, 12:08
 */

package sit.tools.xml;
import sit.tools.xml.XMLLayerUtils;
import sit.GlobalConsts;

/**
 *
 * @author Simon Thiel
 */
public class XMLAccess {
    
    private static final XMLAccess singleton = new XMLAccess();
    
    /**debughelper*/
    private sit.tools.DebugHelper dbg = sit.tools.DebugHelper.getInstance();
    
    
    private XMLAccess(){
    }
    
    public org.w3c.dom.Document parseXML(String xmlInput){
        dbg.print("parsing "+xmlInput.length()+" chars of xmlcode...",GlobalConsts.DEBUG_XML);
        
        javax.xml.parsers.DocumentBuilderFactory factory
        = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document document = null;
        
        try {
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            java.io.InputStream inst =  new java.io.ByteArrayInputStream(xmlInput.getBytes());
            document = builder.parse(inst);
            dbg.print("parse done",GlobalConsts.DEBUG_XML);
            
        }
        catch (org.xml.sax.SAXException sxe) {
            // Error generated during parsing)
            Exception  x = sxe;
            if (sxe.getException() != null)
                x = sxe.getException();
            x.printStackTrace();
        }
        catch (javax.xml.parsers.ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
            
        }
        catch(java.io.IOException exp){
            exp.printStackTrace();
        }
        return document;
    }
    
    public XMLLayerUtils readXMLInput(XMLLayerUtils startLevel, String nodeRoot, String xmlInput){

        org.w3c.dom.Node aktNode = parseXML(xmlInput);
        
        while(aktNode != null){
            dbg.print("aktnode:" + aktNode.getNodeName(),GlobalConsts.DEBUG_XML);
            if (aktNode.getNodeName().equals(nodeRoot)){
                startLevel.readXMLInput(aktNode);
                break; //stop after the first found element
            }
            aktNode = aktNode.getNextSibling();
            
        }
        
        
        dbg.print("xmlInput processed",GlobalConsts.DEBUG_XML);
        return startLevel;
    }
    
    public String toXML(XMLLayerUtils startLevel){
        StringBuffer result = new StringBuffer("<?xml version=\"1.0\"?>");
        result.append(startLevel.toXML());
        result.append("\n");
        return result.toString();
    }
    
    
    /*public String toImportXML(ConceptList allConcepts){
     
        java.util.Iterator iter;
        Concept tmpConcept;
        StringBuffer result = new StringBuffer("<?xml version=\"1.0\"?>");
     
        iter = allConcepts.getConceptIter();
        while(iter.hasNext()){
            result.append(((Concept)iter.next()).toImportXML());
        }
     
        iter = allConcepts.getConceptIter();
        while(iter.hasNext()){
            tmpConcept = (Concept)iter.next();
            result.append(tmpConcept.getRelationList().toImportXML(allConcepts,tmpConcept));
        }
     
        return result.toString();
    }*/
    
    public static XMLAccess getInstance(){
        return singleton;
    }
    
}
