package ru.otus.erinary.h2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.model.Account;
import ru.otus.erinary.model.User;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class H2DataBaseTest {

    private static H2DataBase dataBase;

    @BeforeEach
    void setup() throws SQLException {
        dataBase = new H2DataBase();
        System.out.println("H2DataBase created");
        dataBase.createUserTable();
        System.out.println("Table 'User' created");
        dataBase.createAccountTable();
        System.out.println("Table 'Account' created");
    }

    @AfterEach
    void cleanup() throws SQLException {
        dataBase.close();
    }

    @Test
    void testH2DataBase() throws SQLException {
        dataBase.insertUser("John Doe", 25);
        assertEquals(new User(1, "John Doe", 25), dataBase.selectUserById(1));
        dataBase.insertAccount("VISA", new BigDecimal(5000));
        assertEquals(new Account(1, "VISA", new BigDecimal(5000)), dataBase.selectAccountById(1));
    }

}