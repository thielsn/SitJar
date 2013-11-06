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
 *  Description of PrintCharSets
 * 
 *  @author Simon Thiel
 *  @date 26.06.2012
 */
package sit.simplewebserver;

import java.nio.charset.Charset;

/**
 * PrintCharSets
 * 
 */
public class PrintCharSets {
 /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            System.out.println("displayname;name;toString");
        for (Charset charSet:Charset.availableCharsets().values()){
//            if (!charSet.displayName().contains(charSet.name())
//                    || !charSet.displayName().contains(charSet.toString()))
//            
            System.out.println(charSet.displayName()+";"+charSet.name()+";"+charSet.toString());
        }
     
        
        
    }
}
