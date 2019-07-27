package ru.otus.erinary.atm.payment;

import ru.otus.erinary.atm.ATM;
import ru.otus.erinary.atm.Denomination;

import java.util.Map;

/**
 * Интерфейс стратегий выдачи денег
 */
public interface PaymentStrategy {

    Map<Denomination, Long> getMoney(ATM atm, long requestedAmount);

}
