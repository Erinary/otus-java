package ru.otus.erinary.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DBServiceImpl<T> implements DBService<T> {

    private final SessionFactory sessionFactory;
    private final Class<T> entityType;

    public DBServiceImpl(SessionFactory sessionFactory, Class<T> entityType) {
        this.sessionFactory = sessionFactory;
        this.entityType = entityType;
    }

    @Override
    public void create(T objectData) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(objectData);
            session.getTransaction().commit();
        }
    }

    @Override
    public T load(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(entityType, id);
        }
    }

}
