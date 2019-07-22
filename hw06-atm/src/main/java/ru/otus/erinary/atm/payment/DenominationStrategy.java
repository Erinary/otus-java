package ru.otus.erinary.atm.payment;

import ru.otus.erinary.atm.ATM;
import ru.otus.erinary.atm.Cell;

import java.util.Map;

/**
 * Стратегия выдачи денег с указанием от клиента, какие банкноты нужны
 */
public class DenominationStrategy implements PaymentStrategy {

    private ATM atm;

    @Override
    public void setATM(ATM atm) {
        this.atm = atm;
    }

    @Override
    public Map<Cell.Denomination, Long> getMoney(long requestedAmount) {
        return null;
    }
}
