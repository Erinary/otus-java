package ru.otus.erinary.h2;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.model.User;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class H2DataBaseTest {

    private static final String URL = "jdbc:h2:mem:users_db";
    private static H2DataBase dataBase;

    @BeforeEach
    void setup() throws SQLException {
        dataBase = new H2DataBase(URL);
        System.out.println("H2DataBase created");
    }

    @AfterEach
    void cleanup() throws SQLException {
        dataBase.close();
    }

    @Test
    void testH2DataBase() throws SQLException {
        dataBase.insertUser("John Doe", 25);
        assertEquals(new User(1, "John Doe", 25), dataBase.selectUserById(1));
    }

}