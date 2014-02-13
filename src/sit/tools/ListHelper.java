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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author simon
 */
public class ListHelper {
    public static <T> Vector<T> listToVector(List<T> list){
        if (list==null){
            return null;
        }
        if (list instanceof Vector){
            return (Vector<T>) (Object) list;
        }//else

        Vector<T> result = new Vector(list.size());
        for (T t : list) {
            result.add(t);
        }
        return result;
    }

    public static <T> Vector<T> entryToVector(T entry){
        Vector<T> result = new Vector();
        result.add(entry);
        return result;
    }

    public static <T> ArrayList<T> entryToArrayList(T entry){
        ArrayList<T> result = new ArrayList();
        result.add(entry);
        return result;
    }


}
