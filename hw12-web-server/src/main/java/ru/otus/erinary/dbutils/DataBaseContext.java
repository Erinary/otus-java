package ru.otus.erinary.dbutils;

import lombok.Data;
import org.hibernate.SessionFactory;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.orm.HibernateUtil;

import java.sql.SQLException;

@Data
public class DataBaseContext {

    private static final String DB_URL = "jdbc:h2:mem:users_db";
    private H2DataBase dataBase;
    private SessionFactory sessionFactory;

    public DataBaseContext() throws SQLException {
        this.dataBase = new H2DataBase(DB_URL);
        this.sessionFactory = HibernateUtil.getSessionFactory(DB_URL);
    }

    public void cleanup() throws SQLException {
        sessionFactory.close();
        dataBase.close();
    }
}
