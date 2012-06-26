/*
 *  Description of PrintCharSets
 * 
 *  @author Simon Thiel
 *  @version $Revision: $
 *  @date 26.06.2012
 */
package sit.simplewebserver;

import java.nio.charset.Charset;

/**
 * PrintCharSets
 * 
 */
public class PrintCharSets {
 /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            System.out.println("displayname;name;toString");
        for (Charset charSet:Charset.availableCharsets().values()){
//            if (!charSet.displayName().contains(charSet.name())
//                    || !charSet.displayName().contains(charSet.toString()))
//            
            System.out.println(charSet.displayName()+";"+charSet.name()+";"+charSet.toString());
        }
        
        
        System.out.println("012345".length()+"-" + "012345".substring(6)+"-");
    }
}
