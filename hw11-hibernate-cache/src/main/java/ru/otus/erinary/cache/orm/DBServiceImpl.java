package ru.otus.erinary.cache.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import ru.otus.erinary.cache.orm.engine.CacheEngine;

import javax.persistence.Id;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

public class DBServiceImpl<T> implements DBService<T> {

    private final CacheEngine<Long, T> cache;
    private final SessionFactory sessionFactory;
    private final Class<T> entityType;

    public DBServiceImpl(SessionFactory sessionFactory, Class<T> entityType, CacheEngine<Long, T> cache) {
        this.sessionFactory = sessionFactory;
        this.entityType = entityType;
        this.cache = cache;
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
        T result = cache.get(id);
        if (result == null) {
            Session session = sessionFactory.openSession();
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
        }
        if (result != null) {
            cache.put(id, result);
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

    @Override
    public CacheEngine getCache() {
        return cache;
    }

}
