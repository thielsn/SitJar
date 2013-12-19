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

/**
 * HashMapSet.java
 *
 * 28-Apr-2010
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 */


package sit.sstl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * class HashMapSet
 *
 * @param <K>
 * @param <V>  
 */
public class HashMapSet<K,V extends ObjectWithKey<K>>
    implements Iterable<V>, Serializable{

    private final HashMap<K,V> set = new HashMap();

    public synchronized void clear(){
        set.clear();
    }

    public synchronized void add(V item){
        set.put(item.getKey(), item);
    }

    public synchronized V remove(K id){
        return set.remove(id);
    }

    public synchronized boolean contains(V object){
        return set.containsKey(object.getKey());
    }
    
    public synchronized boolean contains(K key){
        return set.containsKey(key);
    }
    
    public synchronized Iterator<V> iterator(){
        return set.values().iterator();
    }

    /**
     * Returns the object to which the specified key is mapped, or null 
     * if this map contains no mapping for the key.
     * 
     * @param id
     * @return 
     */
    public synchronized V get(K id){
        return set.get(id);
    }
    
    public synchronized V get(V object){
        return set.get(object.getKey());
    }

    public synchronized int size(){
        return set.size();
    }

    
    @Override
    public synchronized boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HashMapSet<K, V> other = (HashMapSet<K, V>) obj;
        if (this.set != other.set && (this.set == null || !this.set.equals(other.set))) {
            return false;
        }
        return true;
    }

    @Override
    public synchronized int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.set != null ? this.set.hashCode() : 0);
        return hash;
    }

    
   
}
