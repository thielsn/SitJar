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

package sit.tools;

import java.util.List;

/**
 *
 * @author simon
 */
public class EnumHelper {

    public static <T extends Enum> boolean elementIsInArray(T element, T[] array){
         for (T myElement : array) {
             if (myElement==element){
                 return true;
             }
         }
         return false;
     }

     public static <T extends Enum> boolean elementIsInList(T element, List<T> list){
         for (T myElement : list) {
             if (myElement==element){
                 return true;
             }
         }
         return false;
     }
}
