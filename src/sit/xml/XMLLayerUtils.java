/*
 * XMLLayerUtils.java
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 * Created on 1. September 2004, 11:23
 */
package sit.xml;

/**
 *
 * @author Simon Thiel
 */
public interface XMLLayerUtils {

    public String toXML();

    public void readXMLInput(org.w3c.dom.Node xmlInput);
}
