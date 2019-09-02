package ru.otus.erinary.department;

import ru.otus.erinary.department.atm.ATM;
import ru.otus.erinary.department.atm.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.otus.erinary.department.atm.Denomination.*;

public class Department {

    private final ATMCaretaker caretaker;
    private final Map<String, ATM> atms;

    public Department() {
        this.caretaker = new ATMCaretaker();
        this.atms = new HashMap<>();
    }

    public ATM getATM(String id) {
        return atms.get(id);
    }

    public void restoreAllATM() {
        atms.forEach((key, value) -> restoreATMState(key));
    }

    public void restoreATMState(String key) {
        caretaker.restoreATMState(atms.get(key));
    }

    public long getAllATMBalance() {
        return atms.values().stream().mapToLong(ATM::getATMBalance).sum();
    }

    public String createATM(List<Cell> cells) {
        String id = UUID.randomUUID().toString();
        if (atms.containsKey(id)) {
            id = UUID.randomUUID().toString();
        }
        ATM atm = new ATM(id, cells);
        atms.put(id, atm);
        saveATMState(atm);
        return id;
    }

    public String createDefaultATM() {
        List<Cell> cells = List.of(
                new Cell(HUNDRED, 10),
                new Cell(FIVE_HUNDRED, 10),
                new Cell(THOUSAND, 10),
                new Cell(FIVE_THOUSAND, 10)
        );
        return createATM(cells);
    }

    public void saveATMState(ATM atm) {
        caretaker.saveATMState(atm);
    }

}
