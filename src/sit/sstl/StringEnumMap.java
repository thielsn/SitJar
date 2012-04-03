/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.sstl;

/**
 * 
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class StringEnumMap<T extends Enum<T>> extends SITEnumMap<T, String> {

    public StringEnumMap(Class type, final Enum[] keys, final String[] values) {
        super(type, keys, values);
    }
}
