/**
 * HashTableSet.java
 *
 * 28-Apr-2010
 *
 * @author Simon Thiel <simon.thiel@iao.fraunhofer.de>
 */


package sit.sstl;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * class HashTableSet
 *
 */
public class HashTableSet<K,V extends ObjectWithKey<K>>
    implements Iterable<V>{

    private Hashtable<K,V> set = new Hashtable();

    public void clear(){
        set.clear();
    }

    public void add(V item){
        set.put(item.getKey(), item);
    }

    public V remove(K id){
        return set.remove(id);
    }

    public Iterator<V> iterator(){
        return set.values().iterator();
    }

    public V get(K id){
        return set.get(id);
    }

    public int size(){
        return set.size();
    }


}
