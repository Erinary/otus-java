package ru.otus.erinary.h2;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.model.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class H2DataBaseTest {

    private static H2DataBase dataBase;

    @BeforeEach
    void setup() throws SQLException {
        dataBase = new H2DataBase();
        System.out.println("H2DataBase created");
        dataBase.createTables();
        System.out.println("Tables 'Users', 'Addresses', 'Phones' created");
    }

    @AfterEach
    void cleanup() throws SQLException {
        dataBase.close();
    }

    @Test
    void testH2DataBase() throws SQLException {
        dataBase.insertUser("John Doe", 25);
        assertEquals(new User(1, "John Doe", 25, null, null), dataBase.selectUserById(1));
    }

}