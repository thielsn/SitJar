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

    public synchronized V get(K id){
        return set.get(id);
    }

    public synchronized int size(){
        return set.size();
    }


}
