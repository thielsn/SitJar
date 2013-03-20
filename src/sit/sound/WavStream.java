package sit.sound;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import sit.sstl.ByteBuilder;



public class WavStream{

    public static class WavStreamException extends Exception {

        public WavStreamException(String message) {
            super(message);
        }
    }

   
    private enum IOState {

        READING, WRITING, CLOSED
    };
    
    private final static int BUFFER_SIZE = 4096;
    private final static int FMT_CHUNK_ID = 0x20746D66;
    private final static int DATA_CHUNK_ID = 0x61746164;
    private final static int RIFF_CHUNK_ID = 0x46464952;
    private final static int RIFF_TYPE_ID = 0x45564157;
    
    
    private int bytesPerSample;			// Number of bytes required to store a single sample
    private long numFrames;					// Number of frames within the data section
    
    private double floatScale;				// Scaling factor used for int <-> float conversion				
    private double floatOffset;			// Offset factor used for int <-> float conversion				
    private boolean wordAlignAdjust;		// Specify if an extra byte at the end of the data chunk is required for word alignment
    // Wav Header
    private int numChannels;				// 2 bytes unsigned, 0x0001 (1) to 0xFFFF (65,535)
    private long sampleRate;				// 4 bytes unsigned, 0x00000001 (1) to 0xFFFFFFFF (4,294,967,295)
    // Although a java int is 4 bytes, it is signed, so need to use a long
    private int blockAlign;					// 2 bytes unsigned, 0x0001 (1) to 0xFFFF (65,535)
    private int validBits;					// 2 bytes unsigned, 0x0002 (2) to 0xFFFF (65,535)
    // Buffering
    private byte[] buffer;					// Local buffer used for IO
    private int bufferPointer;				// Points to the current position in local buffer
    
    private long frameCounter;				// Current number of frames read or written

    private ByteBuilder outBuffer = new ByteBuilder();
    
    /**
     * Cannot instantiate WavStream directly
     */    
     private WavStream(int numChannels, long numFrames, int validBits, long sampleRate) {
        buffer = new byte[BUFFER_SIZE];
        this.numChannels = numChannels;
        this.numFrames = numFrames;
        this.validBits = validBits;
        this.sampleRate = sampleRate;
        this.bytesPerSample = (validBits + 7) / 8;
        this.blockAlign = this.bytesPerSample * numChannels;
    }

    private void init() throws WavStreamException{
        

        // Sanity check arguments
        if (numChannels < 1 || numChannels > 65535) {
            throw new WavStreamException("Illegal number of channels, valid range 1 to 65536");
        }
        if (numFrames < 0) {
            throw new WavStreamException("Number of frames must be positive");
        }
        if (validBits < 2 || validBits > 65535) {
            throw new WavStreamException("Illegal number of valid bits, valid range 2 to 65536");
        }
        if (sampleRate < 0) {
            throw new WavStreamException("Sample rate must be positive");
        }

        // Calculate the chunk sizes
        long dataChunkSize = this.blockAlign * numFrames;
        long mainChunkSize = 4 + // Riff Type
                8 + // Format ID and size
                16 + // Format data
                8 + // Data ID and size
                dataChunkSize;

        // Chunks must be word aligned, so if odd number of audio data bytes
        // adjust the main chunk size
        if (dataChunkSize % 2 == 1) {
            mainChunkSize += 1;
            this.wordAlignAdjust = true;
        } else {
            this.wordAlignAdjust = false;
        }

        // Set the main chunk size
        putLE(RIFF_CHUNK_ID, this.buffer, 0, 4);
        putLE(mainChunkSize, this.buffer, 4, 4);
        putLE(RIFF_TYPE_ID, this.buffer, 8, 4);

        // Write out the header
        this.outBuffer.append(this.buffer, 12);

        // Put format data in buffer
        long averageBytesPerSecond = sampleRate * this.blockAlign;

        putLE(FMT_CHUNK_ID, this.buffer, 0, 4);		// Chunk ID
        putLE(16, this.buffer, 4, 4);		// Chunk Data Size
        putLE(1, this.buffer, 8, 2);		// Compression Code (Uncompressed)
        putLE(numChannels, this.buffer, 10, 2);		// Number of channels
        putLE(sampleRate, this.buffer, 12, 4);		// Sample Rate
        putLE(averageBytesPerSecond, this.buffer, 16, 4);		// Average Bytes Per Second
        putLE(this.blockAlign, this.buffer, 20, 2);		// Block Align
        putLE(validBits, this.buffer, 22, 2);		// Valid Bits

        // Write Format Chunk
        this.outBuffer.append(this.buffer, 24);

        // Start Data Chunk
        putLE(DATA_CHUNK_ID, this.buffer, 0, 4);		// Chunk ID
        putLE(dataChunkSize, this.buffer, 4, 4);		// Chunk Data Size

        // Write Format Chunk
        this.outBuffer.append(this.buffer, 8);

        // Calculate the scaling factor for converting to a normalised double
        if (this.validBits > 8) {
            // If more than 8 validBits, data is signed
            // Conversion required multiplying by magnitude of max positive value
            this.floatOffset = 0;
            this.floatScale = Long.MAX_VALUE >> (64 - this.validBits);
        } else {
            // Else if 8 or less validBits, data is unsigned
            // Conversion required dividing by max positive value
            this.floatOffset = 1;
            this.floatScale = 0.5 * ((1 << this.validBits) - 1);
        }

        this.bufferPointer = 0;
        this.frameCounter = 0;
  
    }

     
    public int getNumChannels() {
        return numChannels;
    }

    public long getNumFrames() {
        return numFrames;
    }

    public long getFramesRemaining() {
        return numFrames - frameCounter;
    }

    public long getSampleRate() {
        return sampleRate;
    }

    public int getValidBits() {
        return validBits;
    }

    public static WavStream newWavStream(int numChannels, long numFrames, int validBits, long sampleRate) throws IOException, WavStreamException {
        // Instantiate new Wavfile and initialise
        WavStream wavStream = new WavStream(numChannels, numFrames, validBits, sampleRate);
        wavStream.init();
       
        return wavStream;
    }


    // Get and Put little endian data from local buffer
    // ------------------------------------------------
    private static long getLE(byte[] buffer, int pos, int numBytes) {
        numBytes--;
        pos += numBytes;

        long val = buffer[pos] & 0xFF;
        for (int b = 0; b < numBytes; b++) {
            val = (val << 8) + (buffer[--pos] & 0xFF);
        }

        return val;
    }

    private static void putLE(long val, byte[] buffer, int pos, int numBytes) {
        for (int b = 0; b < numBytes; b++) {
            buffer[pos] = (byte) (val & 0xFF);
            val >>= 8;
            pos++;
        }
    }

    // Sample Writing and Reading
    // --------------------------
    private void writeSample(long val)  {

        for (int b = 0; b < bytesPerSample; b++) {
            if (bufferPointer == BUFFER_SIZE) {
                outBuffer.append(buffer, BUFFER_SIZE);
                bufferPointer = 0;
            }

            buffer[bufferPointer] = (byte) (val & 0xFF);
            val >>= 8;
            bufferPointer++;
        }
    }


    public int writeFrames(double[] sampleBuffer, int numFramesToWrite) {
        return writeFrames(sampleBuffer, 0, numFramesToWrite);
    }

    public int writeFrames(double[] sampleBuffer, int offset, int numFramesToWrite) {
      
        for (int f = 0; f < numFramesToWrite; f++) {
            if (frameCounter == numFrames) {
                return f;
            }

            for (int c = 0; c < numChannels; c++) {
                writeSample((long) (floatScale * (floatOffset + sampleBuffer[offset])));
                offset++;
            }

            frameCounter++;
        }

        return numFramesToWrite;
    }

    public int writeFrames(double[][] sampleBuffer, int numFramesToWrite)  {
        return writeFrames(sampleBuffer, 0, numFramesToWrite);
    }

    public int writeFrames(double[][] sampleBuffer, int offset, int numFramesToWrite) {

        for (int f = 0; f < numFramesToWrite; f++) {
            if (frameCounter == numFrames) {
                return f;
            }

            for (int c = 0; c < numChannels; c++) {
                writeSample((long) (floatScale * (floatOffset + sampleBuffer[c][offset])));
            }

            offset++;
            frameCounter++;
        }

        return numFramesToWrite;
    }

    public void close() {     

            // If an extra byte is required for word alignment, add it to the end
            if (wordAlignAdjust) {
                byte[] oneByte = {0};
                outBuffer.append(oneByte);
            }
    }
    
    
    public InputStream getInStream() {
        return new ByteArrayInputStream(outBuffer.toByteArray());
    }


    
}
