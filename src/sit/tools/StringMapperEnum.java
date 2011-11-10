/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.tools;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Just an example for an intelligent enum map with lookup function
 * Easier would be to use the StringEnumMap for general use
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public enum StringMapperEnum {
    ONE("Eins"),
    TWO("Zwei"),
    THREE("Drei")
    ;

     private static final Map<String, StringMapperEnum> lookup
          = new HashMap();

     static {
          for(StringMapperEnum s : StringMapperEnum.values())
               lookup.put(s.getString(), s);
     }

     private String myString;

     private StringMapperEnum(String myString) {
          this.myString = myString;
     }

     public String  getString() { return myString; }

     public static StringMapperEnum get(String myString) {
          return lookup.get(myString); 
     }
    

}
