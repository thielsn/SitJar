/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.tools;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ATTENTION - it is important for any derived class to
 *
 * 1) have a XMLRootElement set 
 * 
 * 2) have getters and setters defined for each
 * member field supported!!!
 *
 * @author simon
 */
@XmlRootElement(namespace = "http://morganasoft.de/")
public abstract class SettingsRoot {
}
