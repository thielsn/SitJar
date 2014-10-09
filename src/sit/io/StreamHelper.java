/*
 * Copyright 2013 Simon Thiel
 * 
 * This file is part of SitJar.
 * 
 * SitJar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * SitJar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>. * 
 */

package sit.io;

import java.io.IOException;
import java.io.InputStream;
import sit.sstl.ByteBuilder;

/**
 *
 * @author simon
 */
public class StreamHelper {

    public static ByteBuilder readInputStream(final InputStream reader) throws IOException{
        ByteBuilder result = new ByteBuilder();
        readInputStream(reader, result);
        return result;
    }

    public static ByteBuilder readInputStream(final InputStream reader, final int bufferSize) throws IOException{
        ByteBuilder result = new ByteBuilder(bufferSize);
        readInputStream(reader, result);
        return result;
    }  
    
    private static void readInputStream(final InputStream reader, ByteBuilder byteBuilder) throws IOException{
    
        byte[] buffer = new byte[byteBuilder.getChunkSize()];

        try {
            int read = 0;
            while (read > -1) {
                read = reader.read(buffer);
                if (read > -1) {
                    byteBuilder.append(buffer, read);
                }
            }
        } finally {
            reader.close();
        }
    }

    public static void main(String[] args) {
//       InputStream stubInputStream = .toInputStream("some test data for my input stream");


    }
}
