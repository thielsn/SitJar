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
* along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.
*/

/*
 * IOString.java
 *
 * convertion functions for convenient handling InputStreams and OutputStreams
 * 
 */


package sit.io;

import java.io.InputStream;
import java.io.OutputStream;



public class IOString {

    public static String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

    
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
