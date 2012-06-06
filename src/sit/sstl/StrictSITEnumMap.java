/*
 *  Description of StrictSITEnumMap
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 07.05.2012
 */
package sit.sstl;

import java.util.EnumMap;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de> 
 */
public class StrictSITEnumMap<T extends Enum<T>, Q extends StrictSITEnumContainer> extends EnumMap<T, Q>  {

    
     public StrictSITEnumMap(Class type, final Q[] values) {
        super(type);
        
        for (int i = 0; i < values.length; i++) {
            this.put((T) values[i].getEnumType(), values[i]);
        }

    }

    public T lookUp(Q value) {

        return (T) value.getEnumType();
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Entry<T, Q> entry : entrySet()) {
            result.append(entry.getKey()).append(" <--> ").append(entry.getValue()).append("\n");
        }
        return result.toString();

    }

    
    
}
