package ru.otus.erinary.atm;

import ru.otus.erinary.atm.exception.ATMServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.otus.erinary.atm.Cell.Denomination;

public class ATM {

    private Map<Denomination, Cell> cells;

    public ATM(List<Cell> cells) {
        this.cells = cells.stream().collect(Collectors.toMap(Cell::getDenomination, cell -> cell));
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
    public Map<Denomination, Long> getMoney(long requestedAmount) {
        Map<Denomination, Long> result = new HashMap<>();
        List<Cell> sortedCells = cells.entrySet().stream().sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .map(Map.Entry::getValue).collect(Collectors.toList());
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
                cells.get(denomination).removeBankNotes(result.get(denomination));
            }
            return result;
        } else {
            throw new ATMServiceException("Запрошенная сумма не может быть выдана");
        }
    }
}
