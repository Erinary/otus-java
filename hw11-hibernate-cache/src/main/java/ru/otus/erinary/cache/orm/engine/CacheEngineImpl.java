package ru.otus.erinary.cache.orm.engine;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.*;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private int hit = 0;
    private int miss = 0;

    private Map<K, SoftReference<V>> cache;
    private final ReferenceQueue<V> queue;

    private static final long TASK_PERIOD = 10000;
    private Timer timer;

    public CacheEngineImpl() {
        this.cache = new HashMap<>();
        this.queue = new ReferenceQueue<>();
        this.timer = new Timer();
        timer.schedule(getCleaningTask(), 0, TASK_PERIOD);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, new SoftReference<>(value, queue));
    }

    @Override
    public V get(K key) {
        Optional<SoftReference<V>> optionalRef = Optional.ofNullable(cache.get(key));
        if (optionalRef.isPresent()) {
            hit++;
        } else {
            miss++;
        }
        return optionalRef.map(SoftReference::get).orElse(null);
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public int getCacheSize() {
        return cache.size();
    }

    private TimerTask getCleaningTask() {
        return new TimerTask() {
            @Override
            public void run() {
                List<Reference> referencesToRemove = new ArrayList<>();
                Reference reference;
                while ((reference = queue.poll()) != null) {
                    referencesToRemove.add(reference);
                }
                if (!referencesToRemove.isEmpty()) {
                    for (Reference ref : referencesToRemove) {
                        cache.entrySet().removeIf(entry -> entry.getValue().equals(ref));
                    }
                    referencesToRemove.clear();
                }
            }
        };
    }

    public void cleanup() {
        timer.cancel();
    }
}
