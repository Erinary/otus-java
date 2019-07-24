package ru.otus.erinary.atm.payment;

import ru.otus.erinary.atm.ATM;
import ru.otus.erinary.atm.Denomination;

import java.util.Map;

/**
 * Интерфейс стратегий выдачи денег
 */
public interface PaymentStrategy {

    void setATM(ATM atm);

    Map<Denomination, Long> getMoney(long requestedAmount);

}
