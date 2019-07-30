package ru.otus.erinary.orm;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.model.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DBServiceTest {

    private static H2DataBase dataBase;
    private static DBService<User> userDBService;

    @BeforeAll
    static void setup() throws SQLException {
        dataBase = new H2DataBase();
        System.out.println("H2DataBase created");
        dataBase.createUserTable();
        System.out.println("Table 'User' created");
        dataBase.insertUser("John Doe", 25);
        userDBService = new DBServiceImpl<>(dataBase.getConnection(), User.class);
    }

    @AfterAll
    static void cleanup() throws SQLException {
        dataBase.close();
    }

    @Test
    void testUserInsertion() throws SQLException {
        User janeDoe = new User("Jane Doe", 23);
        userDBService = new DBServiceImpl<>(dataBase.getConnection(), User.class);
        userDBService.create(janeDoe);
        assertEquals(janeDoe, dataBase.selectUserById(2));
    }

    @Test
    void testUserSelection() throws SQLException {
        User johnDoe = dataBase.selectUserById(1);
        assertEquals(johnDoe, userDBService.load(1, User.class));
        assertNull(userDBService.load(100, User.class));
    }
}
