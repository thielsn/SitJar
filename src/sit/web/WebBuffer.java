/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
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
    
    private static int READ_TIMEOUT_MS = 500; //FIXME adapt to WebServer.getInstance().getTimeOut();
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
     * @param ps 
     * @param  off   Offset from which to start taking bytes
     * @param  len   Number of bytes to write
     */
    public void writeToOutStream(PrintStream ps, int off, int len) {

        ps.write(buf, off, len);
    }

    public void writeToOutStream(PrintStream ps) {
        ps.write(buf, 0, readBytes);
    }

    @Override
    public String toString() {
        return new String(buf, 0, readBytes, HttpConstants.DEFAULT_CHARSET);
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

    //ATTENTION - this is also used to read files by WebWorker!!!!! HACK?
    public int readFromInputStream(InputStream is) throws IOException {

        readBytes = 0;
        Long timeStamp = Calendar.getInstance().getTimeInMillis();
        while (is.available() < 1) {
            try {
                Thread.sleep(WAIT_TIME_OUT_MS);

            } catch (InterruptedException ex) {
                Logger.getLogger(WebBuffer.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (timeStamp + READ_TIMEOUT_MS < Calendar.getInstance().getTimeInMillis()) {                
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
//        System.out.println(">"+this.toString()+"<");
        return readBytes;


    }


    public boolean isMoreDataToRead() {
        return !endOfStream;
    }

    byte[] getBuffer() {
        return buf;
    }
}
