package ru.otus.erinary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.atm.ATM;
import ru.otus.erinary.atm.Cell;
import ru.otus.erinary.atm.Denomination;
import ru.otus.erinary.atm.exception.ATMServiceException;
import ru.otus.erinary.atm.payment.DefaultStrategy;
import ru.otus.erinary.atm.payment.DenominationStrategy;
import ru.otus.erinary.atm.payment.PaymentStrategy;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.erinary.atm.Denomination.*;

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
    void testGetMoneyWithDefaultStrategy() {
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

    @Test
    void testGetMoneyWithDenominationStrategy() {
        Map<Denomination, Long> requestedBanknotes = Map.of(THOUSAND, 10L);
        PaymentStrategy denominationStrategy = new DenominationStrategy(requestedBanknotes);

        assertEquals(66000, testATM.getATMBalance());
        Map<Denomination, Long> money = testATM.getMoney(denominationStrategy, 12700);
        assertEquals(53300, testATM.getATMBalance());
        assertEquals(Map.of(THOUSAND, 10L, FIVE_HUNDRED, 5L, HUNDRED, 2L), money);
    }

    @Test
    void testGetMoneyWithDenominationStrategyFailed() {
        Map<Denomination, Long> requestedBanknotes = Map.of(THOUSAND, 12L);
        PaymentStrategy denominationStrategy = new DenominationStrategy(requestedBanknotes);

        Exception exceptionFirst = assertThrows(ATMServiceException.class,
                () -> testATM.getMoney(denominationStrategy, 12700));
        assertEquals("Сумма не может быть выдана запрошенными купюрами", exceptionFirst.getMessage());
        assertEquals(66000, testATM.getATMBalance());
    }
}
