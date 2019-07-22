package ru.otus.erinary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.atm.ATM;
import ru.otus.erinary.atm.Cell;
import ru.otus.erinary.atm.Cell.Denomination;
import ru.otus.erinary.atm.exception.ATMServiceException;
import ru.otus.erinary.atm.payment.DefaultStrategy;
import ru.otus.erinary.atm.payment.PaymentStrategy;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.erinary.atm.Cell.Denomination.*;

class ATMTest {

    private ATM testATM;

    @BeforeEach
    void setup() {
        List<Cell> cells = List.of(
                new Cell(HUNDRED, 10),
                new Cell(FIVE_HUNDRED, 10),
                new Cell(THOUSAND, 10),
                new Cell(FIVE_THOUSAND, 10)
        );
        testATM = new ATM(cells);
    }

    @Test
    void testGetATMBalance() {
        assertEquals(66000, testATM.getATMBalance());
    }

    @Test
    void testPutMoney() {
        Map<Denomination, Long> payment = Map.of(THOUSAND, 2L);
        assertEquals(66000, testATM.getATMBalance());
        testATM.putMoney(payment);
        assertEquals(68000, testATM.getATMBalance());
    }

    @Test
    void testGetMoney() {
        PaymentStrategy defaultStrategy = new DefaultStrategy();
        assertEquals(66000, testATM.getATMBalance());
        Map<Denomination, Long> money = testATM.getMoney(defaultStrategy, 12700);
        assertEquals(53300, testATM.getATMBalance());
        assertEquals(Map.of(FIVE_THOUSAND, 2L, THOUSAND, 2L, FIVE_HUNDRED, 1L, HUNDRED, 2L), money);

        Exception exceptionFirst = assertThrows(ATMServiceException.class,
                () -> testATM.getMoney(defaultStrategy, 10543));
        assertEquals("Запрошенная сумма не может быть выдана", exceptionFirst.getMessage());
        assertEquals(53300, testATM.getATMBalance());
    }
}
