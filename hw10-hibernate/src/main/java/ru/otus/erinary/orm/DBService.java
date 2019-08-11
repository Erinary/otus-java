package ru.otus.erinary.orm;

public interface DBService<T> {

    void create(T objectData);

    T load(long id);
}
