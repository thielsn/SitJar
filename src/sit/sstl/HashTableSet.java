/**
 * HashTableSet.java
 *
 * 28-Apr-2010
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */


package sit.sstl;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * class HashTableSet
 *
 */
public class HashTableSet<K,V extends ObjectWithKey<K>>
    implements Iterable<V>, Serializable{

    private Hashtable<K,V> set = new Hashtable();

    public synchronized void clear(){
        set.clear();
    }

    public synchronized void add(V item){
        set.put(item.getKey(), item);
    }

    public synchronized V remove(K id){
        return set.remove(id);
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
