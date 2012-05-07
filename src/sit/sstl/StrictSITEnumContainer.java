/*
 *  Description of StrictSITEnumContainer
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 07.05.2012
 */
package sit.sstl;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public interface StrictSITEnumContainer<T extends Enum<T>> {

    public T getEnumType();
    
}
