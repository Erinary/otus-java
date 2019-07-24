package ru.otus.erinary.atm.payment;

import ru.otus.erinary.atm.Denomination;
import ru.otus.erinary.atm.exception.ATMServiceException;

import java.util.HashMap;
import java.util.Map;

/**
 * Стратегия выдачи денег с указанием от клиента, какие банкноты нужны
 */
public class DenominationStrategy extends DefaultStrategy {

    private Map<Denomination, Long> requestedBanknotes;

    public DenominationStrategy(Map<Denomination, Long> requestedBanknotes) {
        this.requestedBanknotes = requestedBanknotes;
    }

    @Override
    public Map<Denomination, Long> getMoney(long requestedAmount) {
        Map<Denomination, Long> result = new HashMap<>();
        long rest = requestedAmount;
        for (Denomination denomination : requestedBanknotes.keySet()) {
            long multiplier = requestedBanknotes.get(denomination);
            if (multiplier <= atm.getCells().get(denomination).getAmount()) {
                result.put(denomination, multiplier);
                rest -= denomination.value * multiplier;
                atm.getCells().get(denomination).removeBankNotes(multiplier);
            } else {
                throw new ATMServiceException("Сумма не может быть выдана запрошенными купюрами");
            }
        }
        Map<Denomination, Long> restPayment = super.getMoney(rest);
        for (Denomination denomination : restPayment.keySet()) {
            if (result.containsKey(denomination)) {
                result.put(denomination, Long.sum(result.get(denomination), restPayment.get(denomination)));
            } else {
                result.put(denomination, restPayment.get(denomination));
            }
        }
        return result;
    }
}
