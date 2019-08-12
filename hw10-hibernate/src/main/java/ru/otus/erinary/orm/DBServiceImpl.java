package ru.otus.erinary.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public class DBServiceImpl<T> implements DBService<T> {

    private final SessionFactory sessionFactory;
    private final Class<T> entityType;

    public DBServiceImpl(SessionFactory sessionFactory, Class<T> entityType) {
        this.sessionFactory = sessionFactory;
        this.entityType = entityType;
    }

    @Override
    public void create(T objectData) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(objectData);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE ||
                    session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                session.getTransaction().rollback();
                throw e;
            }
        } finally {
            session.close();
        }
    }

    @Override
    public T load(long id) {
        Session session = sessionFactory.openSession();
        T result = null;
        try {
            session.beginTransaction();
            result = session.get(entityType, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE ||
                    session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                session.getTransaction().rollback();
                throw e;
            }
        } finally {
            session.close();
        }
        return result;
    }
}
