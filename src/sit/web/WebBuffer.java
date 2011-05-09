/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class WebBuffer {

    public static final int BUF_SIZE = 2048;
    public static final byte[] EOL = {(byte) '\r', (byte) '\n'};
    
    /* buffer to use for requests */
    private byte[] buf = new byte[BUF_SIZE];

    private int readBytes = -1;

     /**
     * Writes <code>len</code> bytes from the specified byte array starting at
     * offset <code>off</code> to the given stream.
     *
     * @param  buf   A byte array
     * @param  off   Offset from which to start taking bytes
     * @param  len   Number of bytes to write
     */
    public void writeToPrintStream(PrintStream ps, int off, int len) {
    
        ps.write(buf, off, len);
    }


    public void writeToPrintStream(PrintStream ps) {
        ps.write(buf, 0, readBytes);
    }


    @Override
    public String toString() {
         return new String(buf, 0, readBytes, Charset.forName("US-ASCII"));
    }

    public byte get(int i) {
        return buf[i];
    }

    public void clear(){
         /* zero out the buffer from last time */
        for (int i = 0; i < BUF_SIZE; i++) {
            buf[i] = 0;
        }
        readBytes = -1;
    }

    public int readFromInputStream(InputStream is) throws IOException {
        readBytes = is.read(buf);
        return readBytes;
    }

//    public int readFirstLineOfBuffer(InputStream is) throws IOException {
//
//
//        /* We only support HTTP GET/HEAD, and don't
//         * support any fancy HTTP options,
//         * so we're only interested really in
//         * the first line.
//         */
//        readBytes = 0;
//        int r = 0;
//
//        outerloop:
//        while (readBytes < BUF_SIZE) {
//            r = is.read(buf, readBytes, BUF_SIZE - readBytes);
//            if (r == -1) {
//                /* EOF */
//                return -1;
//            }
//            int i = readBytes;
//            readBytes += r;
//            for (; i < readBytes; i++) {
//                if (buf[i] == (byte) '\n' || buf[i] == (byte) '\r') {
//                    /* read one line */
//                    break outerloop;
//                }
//            }
//        }
//        return readBytes;
//    }

    public boolean bufferIsFull(){
        return readBytes==BUF_SIZE;
    }




}
