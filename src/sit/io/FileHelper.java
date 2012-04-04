/*
 *  Description of FileHelper
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 04.04.2012
 */
package sit.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class FileHelper implements FileHelperI {

    public static final String lineSep = System.getProperty("line.separator");

    public void writeToFile(String fileName, String content) throws IOException {
        PrintWriter writer = new PrintWriter(new FileOutputStream(fileName));
        writer.write(content);
        writer.close();

    }

    public String readFromTextFile(String fileName) throws FileNotFoundException, IOException{
            String line;
            StringBuilder result = new StringBuilder(1024);
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            while ((line = reader.readLine()) != null) {                
                result.append(line);
                result.append(lineSep);
            }
            reader.close();
            return result.toString();
    }

    public boolean fileExists(String fileName) {
        File f = new File(fileName);
        return f.exists();
    }
}
