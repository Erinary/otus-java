package ru.otus.erinary.department.atm;

import lombok.Data;
import ru.otus.erinary.department.atm.visitor.ATMVisitor;
import ru.otus.erinary.department.exception.ATMServiceException;
import ru.otus.erinary.department.payment.PaymentStrategy;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Data
public class ATM {

    private String id;
    private Map<Denomination, Cell> cells;

    public ATM(String id, List<Cell> cells) {
        this.id = id;
        this.cells = new TreeMap<>((d1, d2) -> d2.value - d1.value);
        this.cells.putAll(cells.stream().collect(Collectors.toMap(Cell::getDenomination, cell -> cell)));
    }

    /**
     * Метод получения остатка средств в ATM
     *
     * @return остаток средств
     */
    public long getATMBalance() {
        long balance = cells.entrySet().stream().mapToLong(entry -> entry.getKey().value * entry.getValue().getAmount()).sum();
        System.out.println("Баланс ATM [" + id + "]: " + balance);
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
        return strategy.getMoney(this, requestedAmount);
    }

    public ATMState getATMState() {
        return new ATMState(id, cells.values().stream()
                .map(c -> new CellState(c.getDenomination(), c.getAmount()))
                .collect(Collectors.toList()));
    }

    public void restoreATMState(ATMState state) {
        if (!Objects.equals(state.getId(), id)) {
            throw new ATMServiceException("Trying to restore state from ATM with different ID");
        }
        cells.clear();
        cells.putAll(state.getCells().stream()
                .collect(Collectors.toMap(
                        CellState::getDenomination,
                        cellState -> new Cell(cellState.getDenomination(), cellState.getAmount())
                )));
    }

    public void accept(ATMVisitor visitor) {
        visitor.visit(this);
    }

}
