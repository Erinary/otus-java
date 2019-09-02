package ru.otus.erinary.department;

import ru.otus.erinary.department.atm.ATM;
import ru.otus.erinary.department.atm.ATMState;
import ru.otus.erinary.department.atm.Cell;
import ru.otus.erinary.department.exception.ATMServiceException;

import java.util.*;

import static ru.otus.erinary.department.atm.Denomination.*;

@SuppressWarnings("WeakerAccess")
public class Department {

    private final Map<String, ATM> atms;
    private final Map<String, ATMState> atmInitialStates;

    public Department() {
        this.atms = new HashMap<>();
        this.atmInitialStates = new HashMap<>();
    }

    public ATM getATM(String id) {
        return atms.get(id);
    }

    public void restoreAllATM() {
        atms.forEach((key, value) -> restoreATMState(key));
    }

    public void restoreATMState(String key) {
        Optional.ofNullable(getATM(key))
                .orElseThrow(() -> new ATMServiceException("ATM not found"))
                .restoreATMState(atmInitialStates.get(key));
    }

    public long getAllATMBalance() {
        return atms.values().stream().mapToLong(ATM::getATMBalance).sum();
    }

    public String createATM(List<Cell> cells) {
        String id = UUID.randomUUID().toString();
        while (atms.containsKey(id)) {
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
        atmInitialStates.put(atm.getId(), atm.getATMState());
    }

}
