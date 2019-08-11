package ru.otus.erinary.orm;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.otus.erinary.model.Address;
import ru.otus.erinary.model.Phone;
import ru.otus.erinary.model.User;

public class HibernateUtil {

    public static SessionFactory getSessionFactory(String url) {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .applySetting("hibernate.connection.url", url)
                .build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        return metadataSources
                .addAnnotatedClass(User.class)
//                .addAnnotatedClass(Address.class)
//                .addAnnotatedClass(Phone.class)
                .getMetadataBuilder().build().getSessionFactoryBuilder().build();
    }
}
