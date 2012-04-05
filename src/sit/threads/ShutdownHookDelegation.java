/*
 *  Description of ShutdownHookDelegation
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 05.04.2012
 */

package sit.threads;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public interface ShutdownHookDelegation {
    public void executeOnShutdown();
}
