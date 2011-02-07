package sit.io;

/*
 * IOString.java
 */

import java.io.*;

public class IOString {

    private StringBuffer buf;

    /** Creates a new instance of IOString */
    public IOString() {
        buf = new StringBuffer();
    }

    public IOString(String text) {
        buf = new StringBuffer(text);
    }

    public InputStream getInputStream() {
        return new IOString.IOStringInputStream();
    }

    public OutputStream getOutputStream() {
        return new IOString.IOStringOutputStream();
    }

    public String getString() {
        return buf.toString();
    }

    class IOStringInputStream extends java.io.InputStream {

        private int position = 0;

        public int read() throws java.io.IOException {
            if (position < buf.length()) {
                return buf.charAt(position++);
            } else {
                return -1;
            }
        }
    }

    class IOStringOutputStream extends java.io.OutputStream {

        public void write(int character) throws java.io.IOException {
            buf.append((char) character);
        }
    }

    public static void main(String[] args) {
        IOString target = new IOString();
        IOString source = new IOString("Hello World.");
        convert(target.getOutputStream(), source.getInputStream());
        System.out.println(target.getString());
    }

    /** <CODE>convert</CODE> doesn't actual convert anything but copies byte
    for byte
     */
    public static boolean convert(java.io.OutputStream out, java.io.InputStream in) {
        try {
            int r;
            while ((r = in.read()) != -1) {
                out.write(r);
            }
            return true;
        } catch (java.io.IOException ioe) {
            return false;
        }
    }
}
