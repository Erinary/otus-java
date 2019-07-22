package ru.otus.erinary.atm;

import ru.otus.erinary.atm.payment.PaymentStrategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.otus.erinary.atm.Cell.Denomination;

public class ATM {

    private Map<Denomination, Cell> cells;

    public ATM(List<Cell> cells) {
        this.cells = cells.stream().collect(Collectors.toMap(Cell::getDenomination, cell -> cell));
    }

    public Map<Denomination, Cell> getCells() {
        return cells;
    }

    /**
     * Метод получения остатка средств в ATM
     *
     * @return остаток средств
     */
    public long getATMBalance() {
        long balance = cells.entrySet().stream().mapToLong(entry -> entry.getKey().value * entry.getValue().getAmount()).sum();
        System.out.println("Баланс ATM: " + balance);
        return balance;
    }

    /**
     * Метод внесения средств в ATM
     *
     * @param payment мап с парами: номинал (ключ) - кол-во банкнот (значение)
     */
    public void putMoney(Map<Denomination, Long> payment) {
        for (Denomination denomination : payment.keySet()) {
            cells.get(denomination).addBankNotes(payment.get(denomination));
            System.out.println("Внесено: " + denomination + "*" + payment.get(denomination));
        }
    }

    /**
     * Метод получения средств из ATM
     *
     * @param requestedAmount запрашиваемая сумма
     * @return мап с парами: номинал (ключ) - кол-во банкнот (значение)
     */
    public Map<Denomination, Long> getMoney(PaymentStrategy strategy, long requestedAmount) {
        strategy.setATM(this);
        return strategy.getMoney(requestedAmount);
    }
}
