package ru.otus.erinary.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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

    @Override
    public List<T> loadAll() {
        Session session = sessionFactory.openSession();
        List<T> result = null;
        try {
            session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = criteriaBuilder.createQuery(entityType);
            Root<T> root = query.from(entityType);
            CriteriaQuery<T> all = query.select(root);
            TypedQuery<T> allQuery = session.createQuery(all);
            result = allQuery.getResultList();

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
