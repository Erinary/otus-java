package ru.otus.erinary.department.payment;

import ru.otus.erinary.department.atm.ATM;
import ru.otus.erinary.department.atm.Denomination;

import java.util.Map;

/**
 * Интерфейс стратегий выдачи денег
 */
public interface PaymentStrategy {

    Map<Denomination, Long> getMoney(ATM atm, long requestedAmount);

}
