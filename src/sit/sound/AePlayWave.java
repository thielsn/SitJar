/*
 * AePlayWave.java
 *
 * Created on 10. Mai 2007, 21:55
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 * @version $Revision: $
 */

package sit.sound;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 *
 * @author Simon Thiel
 */
public class AePlayWave implements Runnable {
    
    private String filename;
    private Position curPosition;
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb
    protected boolean stop = false;
    protected boolean halt = false;
    
    
    enum Position {
        LEFT, RIGHT, NORMAL
    };
    
    public AePlayWave(String wavfile) {
        filename = wavfile;
        curPosition = Position.NORMAL;
    }
    
    public AePlayWave(String wavfile, Position p) {
        filename = wavfile;
        curPosition = p;
    }
    
    public void setStop(){
        this.stop=true;
    }
    public synchronized void setFilename(String filename){
        this.filename = filename;
    }
    
    public synchronized void wakeUp(){
        this.notify();
    }
//    public synchronized void halt(){
//       
//       // halt=true;        
//       
//    }
    
    
    protected synchronized void doWaiting(){
        try {
            //wait till notified
            wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void run() {
        while(!stop){
            File soundFile = new File(filename);
            if (!soundFile.exists()) {
                System.err.println("Wave file not found: " + filename);
                return;
            }
            
            AudioInputStream audioInputStream = null;
            try {
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            } catch (UnsupportedAudioFileException e1) {
                e1.printStackTrace();
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
            
            AudioFormat format = audioInputStream.getFormat();
            SourceDataLine auline = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            
            try {
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open(format);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            
            if (auline.isControlSupported(FloatControl.Type.PAN)) {
                FloatControl pan = (FloatControl) auline
                    .getControl(FloatControl.Type.PAN);
                if (curPosition == Position.RIGHT)
                    pan.setValue(1.0f);
                else if (curPosition == Position.LEFT)
                    pan.setValue(-1.0f);
            }
            
            auline.start();
            int nBytesRead = 0;
            byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
            
            try {
                while (nBytesRead != -1) {
                    nBytesRead = audioInputStream.read(abData, 0, abData.length);
                    if (nBytesRead >= 0)
                        auline.write(abData, 0, nBytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } finally {
                auline.drain();
                auline.close();
            }
            doWaiting();            
        }
    }
}
