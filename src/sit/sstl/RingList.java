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

package sit.sstl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author simon
 */
public class RingList<T> implements Collection<T> {
    private final ArrayList<T> list;
    private final int maxSize;

    public RingList(int maxSize) {
        this.list = new ArrayList(maxSize);
        this.maxSize = maxSize;
    }
    
    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator<T> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    public boolean add(T element) {
        if (list.size()==maxSize){
            list.remove(0);
        }
        return list.add(element);
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        boolean result = false;
        for (T element : c){
            result = this.add(element) | result;
        }
        return result;
    }

    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    public void clear() {
        list.clear();
    }

    public T get(int i) {
        return list.get(i);
    }
    
    public T getHead(){
        if (list.isEmpty()){
            return null;
        }
        return list.get(0);
    }
    
    public T getTail(){
        if (list.isEmpty()){
            return null;
        }
        return list.get(list.size()-1);
    }
    
}
