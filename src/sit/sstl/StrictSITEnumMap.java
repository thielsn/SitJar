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
 * @param <T> the enum containing the types
 * @param <Q> the Class implementing StrictSITEnumContainer containing the content for the enum types
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class StrictSITEnumMap<T extends Enum<T>, Q extends StrictSITEnumContainer> extends EnumMap<T, Q> {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param type class of the Enum Type
     * @param values values of the map implementing StrictSITEnumContainer
     */
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

    
    public Q get(T key) {
        return super.get(key);
    }
    
    
}
