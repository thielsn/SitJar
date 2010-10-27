/*
 * GlobalConsts.java
 *
 *
 */

package sit;

/**
 *
 * @author  simon thiel
 */
public class GlobalConsts {
    /** Set global debuglevel [0..100]*/
    public static final int DEBUG_LEVEL = 30;
    
    /**special debuglevels for different purpos*/
    public static final int DEBUG_CLIENT = 10;
    
    //common data structure
    public static final int DEBUG_CDS_DEFAULT = 60;
    public static final int DEBUG_CDS_XML = 60;
    public static final int DEBUG_CDS_ERROR = 0;
    
    public static final int DEBUG_XML = 80;    
    
    //Element Tool
    public static final int DEBUG_ELEMENTOOL = 50;
    
    //CallerClient
    public static final int DEBUG_CALLERCLIENT = 15;
    
    //Code Writer
    public static final int DEBUG_CODEWRITER = 70;
    
    //SOAP GEN
    public static final int DEBUG_SOAPGEN = 70;
    
    //wsdl2java
    public static final int DEBUG_WSDL2JAVA = 70;
    
    
    /**Global Settings*/
    public static final int DEBUG_WARNING = 5;
    public static final int DEBUG_ERROR = 5;
    public static final int DEBUG_MESSAGE = 0;
    
    public static final String SETTINGS_RESOURCES = "";
    public static final String SETTINGS_LOGS = SETTINGS_RESOURCES+"logs/";
    
    
    
    /** Creates a new instance of GlobalConsts */
    public GlobalConsts() {
    }
    
}
