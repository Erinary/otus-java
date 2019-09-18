package ru.otus.erinary.department;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.department.atm.ATM;
import ru.otus.erinary.department.atm.Denomination;
import ru.otus.erinary.department.exception.ATMServiceException;
import ru.otus.erinary.department.payment.DefaultStrategy;
import ru.otus.erinary.department.payment.DenominationStrategy;
import ru.otus.erinary.department.payment.PaymentStrategy;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.erinary.department.atm.Denomination.*;

class DepartmentTest {

    private Department department;

    @BeforeEach
    void setup() {
        department = new Department();
    }

    @Test
    void testRestoreATMState() {
        String id = department.createDefaultATM();
        assertEquals(66000, department.getAllATMBalance());
        ATM atm = department.getATM(id);
        atm.getMoney(new DefaultStrategy(), 12700);
        assertEquals(53300, department.getAllATMBalance());
        department.restoreAllATM();
        assertEquals(66000, department.getAllATMBalance());
    }

    @Test
    void testGetAllATMBalance() {
        department.createDefaultATM();
        assertEquals(66000, department.getAllATMBalance());
        department.createDefaultATM();
        assertEquals(132000, department.getAllATMBalance());
    }

    @Test
    void testGetMoneyWithDefaultStrategy() {
        String id = department.createDefaultATM();
        ATM atm = department.getATM(id);
        PaymentStrategy defaultStrategy = new DefaultStrategy();

        assertEquals(66000, atm.getATMBalance());
        Map<Denomination, Long> money = atm.getMoney(defaultStrategy, 12700);
        assertEquals(53300, atm.getATMBalance());
        assertEquals(Map.of(FIVE_THOUSAND, 2L, THOUSAND, 2L, FIVE_HUNDRED, 1L, HUNDRED, 2L), money);

        Exception exceptionFirst = assertThrows(ATMServiceException.class,
                () -> atm.getMoney(defaultStrategy, 10543));
        assertEquals("Запрошенная сумма не может быть выдана", exceptionFirst.getMessage());
        assertEquals(53300, atm.getATMBalance());
    }

    @Test
    void testGetMoneyWithDenominationStrategy() {
        String id = department.createDefaultATM();
        ATM atm = department.getATM(id);
        Map<Denomination, Long> requestedBanknotes = Map.of(THOUSAND, 10L);
        PaymentStrategy denominationStrategy = new DenominationStrategy(requestedBanknotes);

        assertEquals(66000, atm.getATMBalance());
        Map<Denomination, Long> money = atm.getMoney(denominationStrategy, 12700);
        assertEquals(53300, atm.getATMBalance());
        assertEquals(Map.of(THOUSAND, 10L, FIVE_HUNDRED, 5L, HUNDRED, 2L), money);
    }

}