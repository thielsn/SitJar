/**
 * HashMapSet.java
 *
 * 28-Apr-2010
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
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

    private HashMap<K,V> set = new HashMap();

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
