/*
 *  Description of UUIDGenerator
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 30.04.2012
 */
package sit.tools;

import java.util.UUID;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class UUIDGenerator {

     /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i=0; i<100; i++){
            System.out.println(UUID.randomUUID().toString());
        }
    }
}
