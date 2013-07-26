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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import sit.sstl.ByteBuilder;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class FileHelper implements FileHelperI {

    public static final String lineSep = System.getProperty("line.separator");

    public void writeToFile(String fileName, String content) throws IOException {
        writeToFile(fileName, content, Charset.defaultCharset());

    }


    //##CHARSET_MARKER##
    public void writeToFile(String fileName, String content, Charset charset) throws IOException {
        FileOutputStream writer = new FileOutputStream(fileName);
        writer.write(content.getBytes(charset));
        writer.close();

    }

    public void writeToFile(String fileName, byte[] content) throws IOException {
        FileOutputStream writer = new FileOutputStream(fileName);
        writer.write(content);
        writer.close();

    }

    /**
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public ByteBuilder readFromFile(String fileName) throws FileNotFoundException, IOException {
        byte[] buffer = new byte[512 * 1024];
        ByteBuilder result = new ByteBuilder();

        FileInputStream reader = new FileInputStream(fileName);
        try {
            int read = 0;
            while (read > -1) {
                read = reader.read(buffer);
                if (read > -1) {
                    result.append(buffer);
                }
            }
        } finally {
            reader.close();
        }
        return result;
    }

    /**
     * reads a file line by line Attention! This call potentially changes the
     * line-separators!
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String readFromTextFile(String fileName) throws FileNotFoundException, IOException {
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

    /**
     * checks if path refers to an existing directory (or file) and creates all
     * missing directories if not.
     *
     * @param path as specified for @java.io.File
     * @return true in case the directory already exists or its creation was
     * successful
     */
    public boolean createDirectoriesIfNotExist(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

    /**
     * appends data to given file
     *
     * @param fileName fileName
     * @param data data
     * @throws FileNotFoundException
     */
    public void appendStringToFile(String fileName, String data) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new FileOutputStream(fileName, true));
        writer.write(data);
        writer.close();

    }

    /**
     * returns the directory of a complete filename
     *
     * @param fileName complete filename
     * @return the directory
     */
    public String pureDir(String fileName) {

        return (new File(fileName)).getPath();
    }

    /**
     * returns the (short) filename of a complete filename
     *
     * @param fileName complete filename
     * @return filename
     */
    public String pureFile(String fileName) {
        return (new File(fileName)).getName();
    }

    public boolean validateFileName(String fileName) {
        return fileName.matches("^[^.\\\\/:*?\"<>|]?[^\\\\/:*?\"<>|]*")
                && getValidFileName(fileName).length() > 0;
    }

    public String getValidFileName(String fileName) {
        return getValidFileName(fileName, "");
    }

    public String getValidFileName(String fileName, String defaultReplacement) {
        String newFileName = fileName.replaceAll("^[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", defaultReplacement);
        if (newFileName.length() == 0) {
            throw new IllegalStateException(
                    "File Name " + fileName + " results in a empty fileName!");
        }
        return newFileName;
    }

    public String getExtention(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if ((dotIndex == -1)
                || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }
}
