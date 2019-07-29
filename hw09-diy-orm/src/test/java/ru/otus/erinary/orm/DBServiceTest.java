package ru.otus.erinary.orm;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.model.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DBServiceTest {

    private static H2DataBase dataBase;

    @BeforeAll
    static void setup() throws SQLException {
        dataBase = new H2DataBase();
        System.out.println("H2DataBase created");
        dataBase.createUserTable();
        System.out.println("Table 'User' created");
        dataBase.insertUser("John Doe", 25);
    }

    @AfterAll
    static void cleanup() throws SQLException {
        dataBase.close();
    }

    @Test
    void testUserInsertion() throws SQLException {
        User janeDoe = new User("Jane Doe", 23);
        DBService<User> dbService = new DBServiceImpl<>(dataBase.getConnection(), User.class);
        dbService.create(janeDoe);
        assertEquals(janeDoe, dataBase.selectUserById(2));
    }
}
