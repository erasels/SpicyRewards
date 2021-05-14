package SpicyRewards.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BidiMap<K,V> extends HashMap<K, V> {
    private HashMap<V,K> inversedMap = new HashMap<>();

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return inversedMap.containsKey(value);
    }

    @Override
    public V get(Object key) {
        return super.get(key);
    }

    public K getInverse(V value) {
        return inversedMap.get(value);
    }

    @Override
    public V put(K key, V value) {
        inversedMap.put(value, key);
        return super.put(key, value);
    }

    @Override
    public V remove(Object key) {
        inversedMap.entrySet().removeIf(e -> e.getKey().equals(key));
        return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
        m.forEach((key, value) -> inversedMap.put(value, key));
    }

    @Override
    public void clear() {
        super.clear();
        inversedMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return super.keySet();
    }

    @Override
    public Collection<V> values() {
        return super.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
