package ru.otus.erinary.department;


import ru.otus.erinary.department.atm.ATM;

import java.util.HashMap;
import java.util.Map;

public class ATMCaretaker {

    private final Map<String, ATMState> atmStates;

    public ATMCaretaker() {
        this.atmStates = new HashMap<>();
    }

    public void saveATMState(ATM atm) {
        atmStates.put(atm.getId(), new ATMState(atm));
    }

    public void restoreATMState(ATM atm) {
        atm.setCells(atmStates.get(atm.getId()).getCells());
    }

}
