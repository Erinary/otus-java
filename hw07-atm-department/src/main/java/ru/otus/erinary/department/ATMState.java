package ru.otus.erinary.department;

import lombok.Data;
import ru.otus.erinary.department.atm.ATM;
import ru.otus.erinary.department.atm.Cell;
import ru.otus.erinary.department.atm.Denomination;

import java.util.HashMap;
import java.util.Map;

@Data
public class ATMState {

    private String id;
    private Map<Denomination, Cell> cells;

    public ATMState(ATM atm) {
        this.id = atm.getId();
        this.cells = new HashMap<>();
        atm.getCells().forEach((key, value) -> cells.put(key, new Cell(value.getDenomination(), value.getAmount())));
    }

}
