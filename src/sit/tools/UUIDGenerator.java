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
 *  Description of UUIDGenerator
 * 
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @date 30.04.2012
 */
package sit.tools;

import java.util.UUID;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class UUIDGenerator {

     /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i=0; i<100; i++){
            System.out.println(UUID.randomUUID().toString());
        }
    }
}
