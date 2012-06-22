package sit.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Taken from:
 * http://apachejava.blogspot.com.es/2011/02/hexconversions-convert-string-byte-byte.html
 * and:
 * http://stackoverflow.com/questions/5564643/android-calculating-sha-1-hash-from-file-fastest-algorithm
 */
public class HashHelper {

    /**
     * Convenience method to convert a byte array to a hex string.
     *
     * @param data the byte[] to convert
     * @return String the converted byte[]
     */
    public static String bytesToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            buf.append(byteToHex(data[i]).toUpperCase());
        }
        return (buf.toString());
    }

    /**
     * method to convert a byte to a hex string.
     *
     * @param data the byte to convert
     * @return String the converted byte
     */
    public static String byteToHex(byte data) {
        StringBuilder buf = new StringBuilder();
        buf.append(toHexChar((data >>> 4) & 0x0F));
        buf.append(toHexChar(data & 0x0F));
        return buf.toString();
    }

    /**
     * Convenience method to convert an int to a hex char.
     *
     * @param i the int to convert
     * @return char the converted char
     */
    public static char toHexChar(int i) {
        if ((0 <= i) && (i <= 9)) {
            return (char) ('0' + i);
        } else {
            return (char) ('a' + (i - 10));
        }
    }

    public static String getSHA1FromFile(File file) throws IOException {
        InputStream fis = null;
        byte[] buffer = new byte[65536];

        String result = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            fis = new DigestInputStream(new FileInputStream(file), digest);
            // DigestInputStream needs the file to be read until reaching EOF
            while (fis.read(buffer) != -1) {
            }
            // After reaching EOF, we can get the digest
            result = HashHelper.bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }

        }

        return result;
    }

    /**
     * This function is passed a File name and it returns a md5 hash of this
     * file.
     *
     * @param FileToMd5
     * @return The md5 string
     */
    public static String getMD5FromFile(File file) throws IOException {
        FileInputStream fis = null;
        String result = null;
        try {

            fis = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int len = 0;
            while (-1 != (len = fis.read(buffer))) {
                digest.update(buffer, 0, len);
            }

            /*
             * Int would be to small and apparentelly you have to use a sign and
             * magnitude
             */
            BigInteger bigInt = new BigInteger(1, digest.digest());
            result = bigInt.toString(16);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }

        }
        return result;
    }
}
