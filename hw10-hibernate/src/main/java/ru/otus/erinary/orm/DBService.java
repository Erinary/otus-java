package ru.otus.erinary.orm;

import java.util.List;

public interface DBService<T> {

    void create(T objectData);

    T load(long id);

    List<T> loadAll();
}
