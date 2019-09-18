package ru.otus.erinary.department.payment;

import ru.otus.erinary.department.atm.ATM;
import ru.otus.erinary.department.exception.ATMServiceException;
import ru.otus.erinary.department.atm.Cell;
import ru.otus.erinary.department.atm.Denomination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Стратегия выдачи денег по умолчанию - минимальным кол-вом банкнот
 */
public class DefaultStrategy implements PaymentStrategy {

    @Override
    public Map<Denomination, Long> getMoney(ATM atm, long requestedAmount) {
        Map<Denomination, Long> result = new HashMap<>();
        List<Cell> sortedCells = new ArrayList<>(atm.getCells().values());
        long rest = requestedAmount;
        for (Cell cell : sortedCells) {
            long multiplier = rest / cell.getDenomination().value;
            if (multiplier != 0 && multiplier <= cell.getAmount()) {
                result.put(cell.getDenomination(), multiplier);
                rest -= cell.getDenomination().value * multiplier;
            }
        }
        if (rest == 0) {
            for (Denomination denomination : result.keySet()) {
                atm.getCells().get(denomination).removeBankNotes(result.get(denomination));
            }
            return result;
        } else {
            throw new ATMServiceException("Запрошенная сумма не может быть выдана");
        }
    }
}
