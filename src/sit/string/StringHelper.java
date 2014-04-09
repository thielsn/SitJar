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

package sit.string;

import java.nio.charset.Charset;

/**
 *
 * @author simon
 */
public class StringHelper {

    public static String transformToCharset(String string, Charset newCharset){
        if (string ==null){
            return null;
        }
        return new String(string.getBytes(), newCharset);
    }

    public static String transformCharset(String string, Charset oldCharset){
        if (string ==null){
            return null;
        }
        return new String(string.getBytes(oldCharset));
    }

    public static String transformCharset(String string, Charset oldCharset, Charset newCharset){
        if (string ==null){
            return null;
        }
        return new String(string.getBytes(oldCharset), newCharset);
    }
}
