/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class WebBuffer {

    public static final int BUF_SIZE = 2048;
    public static final byte[] EOL = {(byte) '\r', (byte) '\n'};
    private static final int READ_TIME_OUT_MS = 500;
    private static final int WAIT_TIME_OUT_MS = 5;
    /* buffer to use for requests */
    private byte[] buf = new byte[BUF_SIZE];
    private int readBytes = -1;
    private boolean endOfStream = false;

    public void init() {
        readBytes = -1;
        endOfStream = false;
    }

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

    public int getReadBytes() {
        return readBytes;
    }

    public void clear() {
        /* zero out the buffer from last time */
        for (int i = 0; i < BUF_SIZE; i++) {
            buf[i] = 0;
        }
        readBytes = -1;
    }

    public int readFromInputStream(InputStream is) throws IOException {

//        readBytes = is.read(buf,0,BUF_SIZE);
//        return readBytes;

        readBytes = 0;
        Long timeStamp = Calendar.getInstance().getTimeInMillis();
        while (is.available() < 1) {
            try {
                Thread.sleep(WAIT_TIME_OUT_MS);

            } catch (InterruptedException ex) {
                Logger.getLogger(WebBuffer.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (timeStamp + READ_TIME_OUT_MS < Calendar.getInstance().getTimeInMillis()) {
                readBytes = -1;
                return readBytes;
            }
        }

        while (is.available() > 0 && readBytes < BUF_SIZE) {
            int r = is.read();
            if (r == -1) {
                endOfStream = true;
                return readBytes;
            }
            buf[readBytes] = (byte) r;
            readBytes++;
        }
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
    public boolean isMoreDataToRead() {
//        if (readBytes==-1){
//            Logger.getLogger(WebBuffer.class.getName()).log(Level.FINE, "readBytes==-1);");
//            return true;
//        }
//        if (readBytes==BUF_SIZE){
//            Logger.getLogger(WebBuffer.class.getName()).log(Level.FINE, "readBytes==BUF_SIZE");
//            return true;
//        }
//        Logger.getLogger(WebBuffer.class.getName()).log(Level.FINE, "readBytes="+readBytes);
//        return false;
        return !endOfStream;
    }
}
