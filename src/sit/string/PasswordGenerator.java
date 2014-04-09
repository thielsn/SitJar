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

import java.util.Random;

/**
 *
 * @author simon
 */
public class PasswordGenerator {

    private static final Random rnd = new Random();

    public static String generateRandomAlphaNumPassword(int minLength, int maxLength) {

        int gap = maxLength - minLength;

        if (minLength < 1 || gap < 0) {
            throw new IllegalArgumentException("Requested random string minLenght "
                    + minLength
                    + " is less than 1 or greater then maxLength: " + maxLength);
        }//else
        
        return randomAlphanumeric(rnd.nextInt(gap+1) + minLength);
    }

    /**
     * <p>
     * Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>
     * Characters will be chosen from the set of alpha-numeric characters.</p>
     *
     * @param count the length of random string to create
     * @return the random string
     */
    public static String randomAlphanumeric(int count) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }

        int end = 'z' + 1;
        int start = ' ';

        char[] buffer = new char[count];
        int gap = end - start;

        while (count-- != 0) {
            char ch = (char) (rnd.nextInt(gap) + start);

            if (Character.isLetter(ch)
                    || Character.isDigit(ch)) {
                if (ch >= 56320 && ch <= 57343) {
                    if (count == 0) {
                        count++;
                    } else {
                        // low surrogate, insert high surrogate after putting it in
                        buffer[count] = ch;
                        count--;
                        buffer[count] = (char) (55296 + rnd.nextInt(128));
                    }
                } else if (ch >= 55296 && ch <= 56191) {
                    if (count == 0) {
                        count++;
                    } else {
                        // high surrogate, insert low surrogate before putting it in
                        buffer[count] = (char) (56320 + rnd.nextInt(128));
                        count--;
                        buffer[count] = ch;
                    }
                } else if (ch >= 56192 && ch <= 56319) {
                    // private high surrogate, no effing clue, so skip it
                    count++;
                } else {
                    buffer[count] = ch;
                }
            } else {
                count++;
            }

        }
        return new String(buffer);
    }

    private static void logTest(int min, int max){
        String pwd = generateRandomAlphaNumPassword(min, max);
        System.out.println("min: "+min+" max: "+max+" pwd: "+" ("+pwd.length()+") "+pwd);
    }
    //TEST
    public static void main(String[] args) {
        logTest(5,7);
        
        for (int i = 0; i < 20; i++) {
            logTest(5,5);
        }
        for (int i = 0; i < 20; i++) {
            logTest(5,6);
        }
        
        for (int i = 0; i < 20; i++) {
            logTest(8,20);
        }

        

    }
}
