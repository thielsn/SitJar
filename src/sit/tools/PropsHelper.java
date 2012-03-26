/*
 * PropsHelper.java
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 * Created on 25. Maerz 2006, 22:51
 */

package sit.tools;

/**
 *
 * @author  thiel
 */
public class PropsHelper {
    
    /** singleton */
    protected static PropsHelper myPropsHelper = new PropsHelper();
    
    protected java.util.Hashtable properties = new java.util.Hashtable();
    
    /** Creates a new instance of PropsHelper */
    protected PropsHelper() {
    }
    
    public void addPropertys(DefaultProps myProp){
        properties.put(myProp.getHeader(), myProp);
    }
    
    public DefaultProps getPropertys(String header){
        return (DefaultProps)properties.get(header);
    }
    
    public DefaultProps loadNAddProps(DefaultProps myProp){
        if (!properties.containsKey(myProp.header)){
            
            //if file does not exist
            //write default values to file
            if (!myProp.loadFromFile()){
                myProp.overwriteFileWithDefaults();
            }
            addPropertys(myProp);
        }
        return getPropertys(myProp.header);
    }
    
    
    
    public static PropsHelper getInstance(){
        return myPropsHelper;
    }
}
