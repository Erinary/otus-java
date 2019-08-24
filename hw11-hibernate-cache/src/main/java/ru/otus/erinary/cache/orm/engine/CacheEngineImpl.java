package ru.otus.erinary.cache.orm.engine;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private final int maxElements;
    private int hit = 0;
    private int miss = 0;

    private Map<K, SoftReference<V>> cache;

    public CacheEngineImpl(int maxElements) {
        this.maxElements = maxElements;
        this.cache = new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        if (cache.size() == maxElements) {
            cache.remove(cache.keySet().iterator().next());
        }
        cache.put(key, new SoftReference<>(value));
    }

    @Override
    public V get(K key) {
        V element = cache.get(key).get();
        if (element != null) {
            hit++;
        } else {
            miss++;
        }
        return element;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

}
