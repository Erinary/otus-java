package ru.otus.erinary.cache.orm;

import ru.otus.erinary.cache.orm.engine.CacheEngine;

import java.util.List;

public interface DBService<T> {

    void create(T objectData);

    T load(long id);

    List<T> loadAll();

    CacheEngine getCache();
}
