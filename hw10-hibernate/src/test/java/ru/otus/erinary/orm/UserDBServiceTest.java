package ru.otus.erinary.orm;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.model.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDBServiceTest {
    private static final String DB_URL = "jdbc:h2:mem:users_db";

    private H2DataBase dataBase;
    private SessionFactory sessionFactory;
    private DBService<User> userDBService;

    @BeforeEach
    void setup() throws SQLException {
        dataBase = new H2DataBase(DB_URL);
        sessionFactory = HibernateUtil.getSessionFactory(DB_URL);
        userDBService = new DBServiceImpl<>(sessionFactory, User.class);
        dataBase.insertUser("John Doe", 25);
    }

    @AfterEach
    void cleanup() throws SQLException {
        sessionFactory.close();
        dataBase.close();
    }

    @Test
    void testUserSelection() {
        User johnDoe = userDBService.load(1);
        assertEquals("John Doe", johnDoe.getName());
        assertEquals(25, johnDoe.getAge());
    }

    @Test
    void testUserInsertion() throws SQLException {
        User janeDoe = new User("Jane Doe", 23);
        userDBService.create(janeDoe);
        assertEquals(janeDoe, dataBase.selectUserById(2));
    }
}