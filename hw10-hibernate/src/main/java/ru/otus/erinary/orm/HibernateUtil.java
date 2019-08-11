package ru.otus.erinary.orm;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

class HibernateUtil {

    static SessionFactory getSessionFactory(String url) {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .applySetting("hibernate.connection.url", url)
                .build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        return metadataSources.getMetadataBuilder().build().getSessionFactoryBuilder().build();
    }
}
