package ru.otus.erinary.department.atm.visitor;

import ru.otus.erinary.department.atm.ATM;

public interface ATMVisitor {

    void visit(ATM atm);

}
