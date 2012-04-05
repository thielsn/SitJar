/*
 *  Description of ThreadHelper
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
public class ThreadHelper {

    /**
     * http://www.certpal.com/blogs/2009/08/java-shutdown-hooks/
     *
     * shutdown hook is not guaranteed to execute. If a user closes the app abruptly or the
     * VM crashes, or you click on the little red button on the eclipse console view, the shutdown
     * hook will not run. It is not a good idea to use a shutdown hook to release critical resources
     * (I have seen some code snippets that do this). It might end up not running and cause damage.
     * The shutdown hook will execute only on normal termination or orderly shutdown.
     *
     * Use the shutdown hook if you would like to do trivial operations with it. You can write code i
     * nside the hook that can clean up after the program (say delete a temporary file). Or write a bye
     * bye message. The code inside the hook should not do critical things like release DB connections
     * that your program acquired. Doing something like that is asking for trouble, since this code may
     * never run.
     *
     * Also keep in mind that hooks run concurrently. Each hook is registered as a Thread with the VM and each
     * Thread will run in parallel with the other hooks. If the hooks synchronize over resources incorrectly
     * you will end up dead locking the application.
     *
     * The hooks also need to finish up quickly. If they do not, that poses a
     * problem. The application will wait for ever to exit gracefully.
     *
     *
     * @param delgation
     */
    public static void registerShutdownHook(final ShutdownHookDelegation delgation) {
        Runtime runtime = Runtime.getRuntime();
        Thread shutdownThread = new Thread(new Runnable() {

            private final ShutdownHookDelegation myDelegation = delgation;

            public void run() {
                myDelegation.executeOnShutdown();
            }
        });
        runtime.addShutdownHook(shutdownThread);
    }
}
