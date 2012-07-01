package sit.tools;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon
 */
public class CryptHelper {

    public static String getObscuredContent(String key, String content) {
        return new String(getObscuredContent(key.getBytes(), content.getBytes()));
    }
    
    public static byte[] getObscuredContent(byte[] key, byte[] content) {
        if (key.length==0){
            key =  new byte[]{0};
        }
        if (content.length==0){
            return new byte[0];
        }
        
        byte[] result = new byte[content.length];

        
        for (int i = 0; i < content.length; i++) {
            result[i] = obscure(key[i % key.length], content[i]);
        }
        
        return Base64.encodeBytesToBytes(result);
    }
    
    public static String getUnObscuredContent(String key, String content) throws IOException {
        return new String(getUnObscuredContent(key.getBytes(), content.getBytes()));
    }
    
    public static byte[] getUnObscuredContent(byte[] key, byte[] content) throws IOException {
        if (key.length==0){
            key =  new byte[]{0};
        }
        if (content.length==0){
            return new byte[0];
        }
        
        byte[] result = Base64.decode(content);

        for (int i = 0; i < result.length; i++) {
            result[i] = unobscure(key[(i % key.length)], result[i]);
        }
        
        return result;
    }

    private static String bytesToString(byte[] bytes) {
        String result = "[";
        for (int i = 0; i < bytes.length; i++) {

            result += bytes[i];
            if (i < bytes.length - 1) {
                result += ",";
            }
        }
        return result + "]";
    }

    private static byte obscure(int key, int content) {
        return (byte) ((content + key) % Byte.MAX_VALUE);

    }

    private static byte unobscure(int key, int content) {
        return (byte) ((content - key + Byte.MAX_VALUE) % Byte.MAX_VALUE);

    }

    public static void test(String key, String content) {
        String result = "";
        result += "\n\nkey/clear text/obscure/unobscure\n";
        result += "\"" + key + "\"\n";
        result += "\"" + content + "\"\n";
        String obs = getObscuredContent(key, content);
        result += "\"" + obs + "\"\n";
        try {
            result += "\"" + getUnObscuredContent(key, obs) + "\"\n";
            Logger.getLogger(CryptHelper.class.getName()).log(Level.INFO, result);
        } catch (IOException ex) {
            Logger.getLogger(CryptHelper.class.getName()).log(Level.INFO, result);
            Logger.getLogger(CryptHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        test("blub0", "blub1");
        test("hello world! -++-..ß124ü+", "hello");
        test("hello world! -++-..ß124ü+", "hello32004235ßi0ß32ii5ß23i0i230230");
        test("hello32004235ßi0ß32ii5ß23i0i230230", "hello world! -++-..ß124ü+");
        test("hello world! -++-..ß124ü+", "");
        test("", "hello32004235ßi0ß32ii5ß23i0i230230");

    }
}
