/*
 * Copyright 2009-2010 ZipXap Technology, LLC.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * or visit http://www.gnu.org/licenses/lgpl.html.
 */
package sit.sstl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Helper class for creating a large sequence of bytes. Think of it as the byte
 * equivalent of StringBuilder
 *
 * <p>This class is part of the KavaPL base API.</p> <table>
 * <tr><th>Milestone</th><th>Who</th><th>Date, Time</th></tr> <tr><td>Initial
 * Version</td><td>K Penrose</td><td>June 24, 2007, 5:33 PM</td></tr> </table>
 * <p>** Note that this table is not intended as an exhaustive list of subtle
 * changes. It is intended to list changes that break the compatibility of older
 * code, or major enhancements/rewrites</p>
 *
 * 26/06/2012 - major refactoring by Simon Thiel
 */
public class ByteBuilder {

    
    public static final int DEFAULT_CHUNK_SIZE = 4096;

    /**
     * non synchronized stream!!!
     */
    public static class ByteBuilderOutputStream extends OutputStream {
        private final ByteBuilder byteBuilder;
        private final byte[] buffer;
        private int bufferSize = 0;
        
        public ByteBuilderOutputStream(ByteBuilder byteBuilder) {
            this.byteBuilder = byteBuilder;
            this.buffer = new byte[byteBuilder.getChunkSize()];
        }

        @Override
        public void write(int aByte) throws IOException {
            if (bufferSize>=buffer.length){
                appendBuffer();
            }
            buffer[bufferSize++] = (byte)aByte;
        }
        
        private void appendBuffer(){
            byteBuilder.append(buffer, bufferSize);
            bufferSize = 0;
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            appendBuffer();            
        }

        @Override
        public void close() throws IOException {
            super.close();
            appendBuffer();            
        }
        
        
    }
    
    protected final int chunkSize;

   

    private class SequenceFinder {

        private byte[] searchSeq;
        private int nextByteToFindIndex = 0;
        private int index = 0;
        private int startIndex;

        SequenceFinder(byte[] searchSeq, int startIndex) {
            this.searchSeq = searchSeq;
            this.startIndex = startIndex;
        }

        boolean handleNextByte(byte myByte) {
            if ((index >= startIndex)
                    && (myByte == searchSeq[nextByteToFindIndex])) {
                nextByteToFindIndex++;
                if (nextByteToFindIndex == searchSeq.length) {
                    return true;
                }

            } else {
                nextByteToFindIndex = 0;
            }
            index++;
            return false;
        }

        /**
         * only valid in case handleNextByte returned true
         * TODO better solution would be appreciated
         *
         * @return
         */
        int getResult() {
            return index - searchSeq.length+1;
        }
    }
    private List<byte[]> bytesList = new ArrayList<byte[]>();
    private int startIndexOfFirstChunk = 0;
    private int fillIndex = 0;
    private byte[] response = null;

    public ByteBuilder() {
        chunkSize = DEFAULT_CHUNK_SIZE;
    }

    public ByteBuilder(byte[] bytes) {
        chunkSize = bytes.length;
        append(bytes);
    }

     public OutputStream getOutputStream() {
        return new ByteBuilderOutputStream(this);
    }

    public int getChunkSize() {
        return chunkSize;
    }
    
    public final void append(byte[] bytes) {
        append(bytes, bytes.length);
    }

    /**
     * appends the first 'bytesToAppend' bytes from 'bytes'
     *
     * @param bytes
     * @param bytesToAppend
     */
    public void append(byte[] bytes, int bytesToAppend) {
        if (bytes == null) {
            bytes = new byte[0];
        }
        if (bytesToAppend > bytes.length) {
            throw new RuntimeException("bytesToAppend must be an integer less than or equal to bytes.length. "
                    + "bytesToAppend=" + bytesToAppend + ", bytes.length=" + bytes.length);
        }

        byte[] newBytes;
        response = null; //tell the toByteArray() to reconstruct the final response when called (dirty flag)

        if (bytesList.isEmpty()) {
            newBytes = new byte[chunkSize];
            bytesList.add(newBytes);
            fillIndex = 0;
        } else {
            newBytes = bytesList.get(bytesList.size() - 1);
        }
        for (int i = 0; i < bytesToAppend; i++) {
            if (fillIndex == chunkSize) { //create and add new byte array
                newBytes = new byte[chunkSize];
                bytesList.add(newBytes);
                fillIndex = 0;
            }
            newBytes[fillIndex] = bytes[i];
            fillIndex++;
        }
    }

    /**
     * Discard the first
     * <code>bytesToDiscard</code> bytes from the byte array.
     *
     * @param bytesToDiscard The number of bytes to discard from the byte array.
     */
    public void discard(int bytesToDiscard) {
        int remainingBytes = bytesToDiscard;
        if (remainingBytes < 0 || remainingBytes > size()) {
            throw new RuntimeException("bytesToDiscard must be an integer between 0 and size(). "
                    + "bytesToDiscard=" + remainingBytes + ", size()=" + size());
        }
        response = null; //tell the toByteArray() to reconstruct the final response when called

        while (remainingBytes >= chunkSize) {
            bytesList.remove(0);
            remainingBytes -= chunkSize;
        }
        startIndexOfFirstChunk = remainingBytes;

    }

    /**
     * @return The number of bytes in the ByteBuilder.
     */
    public int size() {
        if (bytesList.isEmpty()) {
            return 0;
        }
        return (bytesList.size() * chunkSize) - (chunkSize - fillIndex) - startIndexOfFirstChunk;
    }

    public byte[] toByteArray() {
        if (response == null) {
            if (size() == 0) //nothing was ever appended
            {
                return new byte[0];
            }
            response = new byte[size()];
            int index = 0;
            for (int i = 0; i < bytesList.size() - 1; i++) {
                byte[] curBytes = bytesList.get(i);
                if (i == 0) {
                    for (int j = startIndexOfFirstChunk; j < chunkSize; j++) {
                        response[index++] = curBytes[j];
                    }
                } else {
                    for (int j = 0; j < chunkSize; j++) {
                        response[index++] = curBytes[j];
                    }
                }
            }
            byte[] curBytes = bytesList.get(bytesList.size() - 1);
            for (int i = 0; i < fillIndex; i++) {
                response[index++] = curBytes[i];
            }
        }
        return response;
    }

    public String toString(Charset charSet) {
        return new String(toByteArray(), charSet);
    }

    /**
     * Returns the index within this ByteArray of the first occurrence of the
     * specified ByteArray.
     *
     * @param sequence
     * @return if the argument occurs as a sequence within this object, then the
     * index of the byte of the first such subsequence is returned; if it does
     * not occur or in case sequence.length>size(), -1 is returned.
     * @throws NullPointerException - if sequence is null.
     */
    public int indexOf(byte[] sequence) {
        return indexOf(0, sequence);
    }

    /**
     * Returns the index within this ByteArray of the first occurrence of the
     * specified ByteArray.
     *
     * @param sequence
     * @param startIndex starting to search from (inclusive)
     * startSearchIndex
     * @return if the argument occurs as a sequence within this object, then the
     * index of the byte of the first such subsequence is returned; if it does
     * not occur or in case sequence.length>size(), -1 is returned.
     * @throws NullPointerException - if sequence is null.
     */
    public int indexOf(int startIndex, byte[] sequence) {

        if (sequence.length > size()) {
            return -1;
        }
        SequenceFinder sq = new SequenceFinder(sequence, startIndex);

        for (int i = 0; i < bytesList.size() - 1; i++) { //traverse the full chunks
            byte[] curBytes = bytesList.get(i);
            if (i == 0) { // skip startIndexOfFirstChunk bytes at the first chunk
                for (int j = startIndexOfFirstChunk; j < chunkSize; j++) {
                    if (sq.handleNextByte(curBytes[j])) {
                        return sq.getResult();
                    }
                }
            } else {
                for (int j = 0; j < chunkSize; j++) {
                    if (sq.handleNextByte(curBytes[j])) {
                        return sq.getResult();
                    }
                }
            }
        }
        byte[] curBytes = bytesList.get(bytesList.size() - 1); //traverse remaining chunk until fillindex
        for (int i = 0; i < fillIndex; i++) {
            if (sq.handleNextByte(curBytes[i])) {
                return sq.getResult();
            }
        }
        return -1;
    }

    /**
     *
     * @param startIndex return subsequence starting at startIndex inclusive
     * until the end of the ByteList
     * @return
     */
    public byte[] subSequence(int startIndex) {
        return subSequence(startIndex, size());
    }

    /**
     *
     * @param startIndex return subsequence starting at startIndex inclusive
     * @param endIndex returning sequence ends at endIndex exclusive - if
     * endIndex>size() subsequence will be reduced to endIndex=size()
     * @return
     */
    public byte[] subSequence(int startIndex, int endIndex) {
        byte[] result = new byte[endIndex - startIndex];
        int index = 0;
        int resIndex = 0;

        for (int i = 0; i < bytesList.size() - 1; i++) { //traverse the full chunks
            byte[] curBytes = bytesList.get(i);
            if (i == 0) { // skip startIndexOfFirstChunk bytes at the first chunk
                for (int j = startIndexOfFirstChunk; j < chunkSize; j++) {
                    if (index >= startIndex && index < endIndex) {
                        result[resIndex++] = curBytes[j];
                    }
                    index++;
                }
            } else {
                for (int j = 0; j < chunkSize; j++) {
                    if (index >= startIndex && index < endIndex) {
                        result[resIndex++] = curBytes[j];
                    }
                    index++;
                }
            }
        }
        byte[] curBytes = bytesList.get(bytesList.size() - 1); //traverse remaining chunk until fillindex
        for (int i = 0; i < fillIndex; i++) {
            if (index >= startIndex && index < endIndex) {
                result[resIndex++] = curBytes[i];
            }
            index++;
        }
        return result;
    }
}