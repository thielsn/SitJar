/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.sound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author simon
 */
public class SoundPlayer {

    private static final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

    private static SourceDataLine getAuLine(AudioFormat format) throws LineUnavailableException {

        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);


        auline = (SourceDataLine) AudioSystem.getLine(info);
        auline.open(format);

        return auline;
    }

    public static void play(File audioFile)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioInputStream.getFormat();
        play(format, audioInputStream);
    }

    public static void play(InputStream stream)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream);
        AudioFormat format = audioInputStream.getFormat();
        play(format, audioInputStream, getAuLine(format));
    }

    public static void play(AudioFormat format, AudioInputStream stream) throws LineUnavailableException, IOException {
        play(format, stream, getAuLine(format));
    }

    public static void play(AudioFormat format, AudioInputStream audioInputStream, SourceDataLine auLine) throws IOException {
        auLine.start();
        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    auLine.write(abData, 0, nBytesRead);
                }
            }
       
        } finally {
            auLine.drain();
            auLine.close();
        }
    }
}
