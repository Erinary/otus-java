package ru.otus.erinary.h2;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.model.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class H2DataBaseTest {

    private static H2DataBase dataBase;

    @BeforeAll
    static void setup() throws SQLException {
        dataBase = new H2DataBase();
        System.out.println("H2DataBase created");
        dataBase.createUserTable();
        System.out.println("Table 'User' created");
    }

    @AfterAll
    static void cleanup() throws SQLException {
        dataBase.close();
    }

    @Test
    void testH2DataBase() throws SQLException {
        dataBase.insertUser(1, "John Doe", 25);
        assertEquals(new User(1, "John Doe", 25), dataBase.selectUserById(1));
    }

}