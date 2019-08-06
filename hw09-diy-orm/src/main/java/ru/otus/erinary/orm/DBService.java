package ru.otus.erinary.orm;

public interface DBService<T> {

    void create(T objectData);

    void update(T objectData);

    void createOrUpdate(T objectData);

    T load(long id);
}
