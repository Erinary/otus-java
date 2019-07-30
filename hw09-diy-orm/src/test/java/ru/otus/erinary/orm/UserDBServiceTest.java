package ru.otus.erinary.orm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.model.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserDBServiceTest {

    private static H2DataBase dataBase;
    private static DBService<User> userDBService;

    @BeforeEach
    void setup() throws SQLException {
        dataBase = new H2DataBase();
        System.out.println("H2DataBase created");
        dataBase.createUserTable();
        System.out.println("Table 'User' created");
        dataBase.insertUser("John Doe", 25);
        dataBase.insertUser("Unknown", 38);
        userDBService = new DBServiceImpl<>(dataBase.getConnection(), User.class);
    }

    @AfterEach
    void cleanup() throws SQLException {
        dataBase.close();
    }

    @Test
    void testUserInsertion() throws SQLException {
        User janeDoe = new User("Jane Doe", 23);
        userDBService.create(janeDoe);
        assertEquals(janeDoe, dataBase.selectUserById(3));
    }

    @Test
    void testUserSelection() throws SQLException {
        User johnDoe = dataBase.selectUserById(1);
        assertEquals(johnDoe, userDBService.load(1));
        assertNull(userDBService.load(100));
    }

    @Test
    void testUserUpdate() throws SQLException {
        User userForUpdate = dataBase.selectUserById(2);
        userForUpdate.setName("John Bull");
        userDBService.update(userForUpdate);
        assertEquals(userForUpdate, dataBase.selectUserById(2));
    }

    @Test
    void testUserCreateOrUpdate() throws SQLException {
        User newUser = new User("Alice", 17);
        userDBService.createOrUpdate(newUser);
        assertEquals(newUser, dataBase.selectUserById(3));
        System.out.println(newUser);
        newUser.setName("Bob");
        userDBService.createOrUpdate(newUser);
        assertEquals(newUser, dataBase.selectUserById(3));
        System.out.println(newUser);
    }
}
