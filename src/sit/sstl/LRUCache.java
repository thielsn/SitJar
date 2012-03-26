/**
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.sstl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * TODO: check potentially more efficient solution using LinkedHashMap:
 * public class LRUCache {
   public static final int MAX_ENTRIES = 50;
   private Map             cache;

   public LRUCache(){
      cache = new LinkedHashMap(MAX_ENTRIES+1, 0.70f, true){
        @Override
        public boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }
    };
}
 *
 *
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class LRUCache<K, V> {

    private HashMap<K, V> map;
    private TreeMap<Long, K> timeMap;
    private int maxEntries;
    private long hashCount = 0;

    public LRUCache(int maxEntries) {
        this.maxEntries = maxEntries;

        map = new HashMap((int)(0.5*maxEntries));
        timeMap = new TreeMap(new Comparator() {

            public int compare(Object o1, Object o2) {
                if (o1 == o2){
                    return 0;
                }
                return (int) ((Long) o1 - (Long) o2);
            }
        });
    }

    private synchronized void trimIfRequired() {
        if (map.size() > maxEntries && !map.isEmpty()) { //trim is required

            K key = timeMap.remove(timeMap.firstKey());
            map.remove(key);

        }
    }

    private synchronized Map.Entry<Long,K> getTimeMapSet(K key){
        
        for (Map.Entry<Long,K> entry :timeMap.entrySet()){
                        
            if (entry.getValue().equals(key)){
                return entry;
            }
        }
        return null;
    }

    public synchronized void reset(){
        hashCount = 0;
        map.clear();
        timeMap.clear();
    }

    private synchronized void updateTimeMap(K key){
        if (map.containsKey(key)){ //key was already in the list
            //remove old entry
            timeMap.remove(getTimeMapSet(key).getKey());
        }
        hashCount++;
        if (hashCount<0){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "HashCount overflow - resetting hash");
            reset();
        }

        timeMap.put(hashCount, key);
    }

    public synchronized void put(K key, V value) {
        updateTimeMap(key);
        map.put(key, value);
        trimIfRequired();
    }

    public synchronized V get(K key) {
        updateTimeMap(key);
        return map.get(key);
    }

    public synchronized boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> myCache = new LRUCache(4);

        myCache.put(1,"a");
        myCache.put(2,"b");
        myCache.put(3,"c");
        myCache.get(1);
        myCache.put(4,"d");
        myCache.put(5,"e");
        myCache.put(6,"f");

        for (int i=0; i<7;i++){
            System.out.println(i+" --> "+myCache.get(i));

        }
        

    }
}
