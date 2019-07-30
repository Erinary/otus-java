package ru.otus.erinary.orm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountDBServiceTest {

    private static H2DataBase dataBase;
    private static DBService<Account> accountDBService;

    @BeforeEach
    void setup() throws SQLException {
        dataBase = new H2DataBase();
        System.out.println("H2DataBase created");
        dataBase.createAccountTable();
        System.out.println("Table 'Account' created");
        dataBase.insertAccount("VISA", new BigDecimal(5000));
        dataBase.insertAccount("MasterCard", new BigDecimal(1000));
        accountDBService = new DBServiceImpl<>(dataBase.getConnection(), Account.class);
    }

    @AfterEach
    void cleanup() throws SQLException {
        dataBase.close();
    }

    @Test
    void testAccountInsertion() throws SQLException {
        Account mir = new Account("MIR", new BigDecimal(2000));
        accountDBService.create(mir);
        assertEquals(mir, dataBase.selectAccountById(3));
    }

    @Test
    void testAccountSelection() throws SQLException {
        Account visa = dataBase.selectAccountById(1);
        assertEquals(visa, accountDBService.load(1));
        assertNull(accountDBService.load(100));
    }

    @Test
    void testAccountUpdate() throws SQLException {
        Account ms = dataBase.selectAccountById(2);
        ms.setRest(new BigDecimal(100));
        accountDBService.update(ms);
        assertEquals(ms, dataBase.selectAccountById(2));
    }

    @Test
    void testAccountCreateOrUpdate() throws SQLException {
        Account zk = new Account("Zolotaya Korona", new BigDecimal(2500));
        accountDBService.createOrUpdate(zk);
        assertEquals(zk, dataBase.selectAccountById(3));
        System.out.println(zk);
        zk.setRest(new BigDecimal(6000));
        accountDBService.createOrUpdate(zk);
        assertEquals(zk, dataBase.selectAccountById(3));
        System.out.println(zk);
    }
}
