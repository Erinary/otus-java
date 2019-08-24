package ru.otus.erinary.cache.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import ru.otus.erinary.cache.orm.engine.CacheEngine;
import ru.otus.erinary.cache.orm.engine.CacheEngineImpl;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DBServiceImpl<T> implements DBService<T> {

    private final SessionFactory sessionFactory;
    private final Class<T> entityType;
    private final CacheEngine<Long, T> cache;

    public DBServiceImpl(SessionFactory sessionFactory, Class<T> entityType, int maxCacheElements) {
        this.sessionFactory = sessionFactory;
        this.entityType = entityType;
        this.cache = new CacheEngineImpl<>(maxCacheElements);
    }

    @Override
    public void create(T objectData) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(objectData);
            session.getTransaction().commit();
            //TODO cache.put(objectData.id{получить присвоенный Id}, objectData)
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
        //TODO cache.get(id), if == null -> лезем в бд, потом кладем cache.put()
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
