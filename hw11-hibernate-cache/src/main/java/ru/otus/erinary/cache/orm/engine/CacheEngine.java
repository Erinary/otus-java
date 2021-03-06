package ru.otus.erinary.cache.orm.engine;

public interface CacheEngine<K, V> {

    void put(K key, V value);

    V get(K key);

    int getHitCount();

    int getMissCount();

    int getCacheSize();

    void cleanup();

}
