/*
 *  Description of FileHelper
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 26.03.2012
 */

package sit.io;

import java.io.IOException;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public interface FileHelper {
    public void writeToFile(String fileName, String content) throws IOException;
}
