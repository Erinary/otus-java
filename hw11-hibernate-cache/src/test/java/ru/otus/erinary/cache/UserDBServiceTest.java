package ru.otus.erinary.cache;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.cache.h2.H2DataBase;
import ru.otus.erinary.cache.model.Address;
import ru.otus.erinary.cache.model.Phone;
import ru.otus.erinary.cache.model.User;
import ru.otus.erinary.cache.orm.DBService;
import ru.otus.erinary.cache.orm.DBServiceImpl;
import ru.otus.erinary.cache.orm.HibernateUtil;
import ru.otus.erinary.cache.orm.engine.CacheEngineImpl;

import java.sql.SQLException;
import java.util.List;
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
        userDBService = new DBServiceImpl<>(sessionFactory, User.class, new CacheEngineImpl<>());
    }

    @AfterEach
    void cleanup() throws SQLException {
        sessionFactory.close();
        dataBase.close();
    }

    @Test
    @DisplayName("Select User from Database")
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

        //cache test
        assertEquals(1, userDBService.getCache().getCacheSize());
        assertEquals(1, userDBService.getCache().getMissCount());
        assertEquals(0, userDBService.getCache().getHitCount());
        User anotherJohnDoe = userDBService.load(1);
        assertEquals(1, userDBService.getCache().getHitCount());
    }

    @Test
    @DisplayName("Insert User into Database")
    void testUserInsertion() throws SQLException {
        Set<Phone> phones = Set.of(new Phone("11111111111"), new Phone("22222222222"));
        Address address = new Address("Another St");
        User janeDoe = new User("Jane Doe", 23, address, phones);
        userDBService.create(janeDoe);

        User dataInBase = dataBase.selectUserById(janeDoe.getId());
        dataInBase.setAddress(dataBase.selectAddressById(janeDoe.getId()));
        dataBase.selectPhoneByUserId(janeDoe.getId()).forEach(dataInBase::addPhone);

        assertEquals(dataInBase, janeDoe);
        assertEquals(0, userDBService.getCache().getCacheSize());
    }

    @Test
    @DisplayName("Select all Users")
    void testSelectAllUsers() {
        Set<Phone> janesPhones = Set.of(new Phone("11111111111"), new Phone("22222222222"));
        Address janesAddress = new Address("Another St");
        User janeDoe = new User("Jane Doe", 23, janesAddress, janesPhones);

        Set<Phone> johnsPhones = Set.of(new Phone("333333333333"), new Phone("44444444444"));
        Address johnAddresses = new Address("Some Bridge Rd");
        User johnDoe = new User("Jane Doe", 23, johnAddresses, johnsPhones);

        userDBService.create(janeDoe);
        userDBService.create(johnDoe);

        List<User> results = userDBService.loadAll();
        assertEquals(2, results.size());
    }
}