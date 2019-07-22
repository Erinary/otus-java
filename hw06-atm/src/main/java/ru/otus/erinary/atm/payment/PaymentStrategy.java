package ru.otus.erinary.atm.payment;

import ru.otus.erinary.atm.ATM;
import ru.otus.erinary.atm.Cell;

import java.util.Map;

/**
 * Интерфейс стратегий выдачи денег
 */
public interface PaymentStrategy {

    void setATM(ATM atm);

    Map<Cell.Denomination, Long> getMoney(long requestedAmount);

}
