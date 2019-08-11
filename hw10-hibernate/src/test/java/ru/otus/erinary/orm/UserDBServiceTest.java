package ru.otus.erinary.orm;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.model.Address;
import ru.otus.erinary.model.Phone;
import ru.otus.erinary.model.User;

import java.sql.SQLException;
import java.util.Set;

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
    }

    @AfterEach
    void cleanup() throws SQLException {
        sessionFactory.close();
        dataBase.close();
    }

    @Test
    void testUserSelection() throws SQLException {
        Set<Phone> phones = Set.of(new Phone(1, "11111111111"), new Phone(2, "22222222222"));
        dataBase.insertUser(1, "John Doe", 25);
        dataBase.insertAddress(1, "Some Bridge Rd");
        dataBase.insertPhone(1, "11111111111", 1);
        dataBase.insertPhone(2, "22222222222", 1);
        User johnDoe = userDBService.load(1);
        assertEquals("John Doe", johnDoe.getName());
        assertEquals(25, johnDoe.getAge());
        assertEquals("Some Bridge Rd", johnDoe.getAddress().getStreet());
        assertEquals(phones, johnDoe.getPhones());
    }

    @Test
    void testUserInsertion() throws SQLException {
        Set<Phone> phones = Set.of(new Phone("11111111111"), new Phone("22222222222"));
        Address address = new Address("Another St");
        User janeDoe = new User("Jane Doe", 23, address, phones);
        userDBService.create(janeDoe);
        User dataInBase = dataBase.selectUserById(janeDoe.getId());
        dataInBase.setAddress(dataBase.selectAddressById(janeDoe.getId()));
        dataBase.selectPhoneByUserId(janeDoe.getId()).forEach(dataInBase::addPhone);
        assertEquals(dataInBase, janeDoe);
    }
}