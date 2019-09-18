package ru.otus.erinary.department.atm.visitor;

import lombok.Getter;
import ru.otus.erinary.department.atm.ATM;

public class BalanceATMVisitor implements ATMVisitor {
    
    @Getter
    private long accumulatedBalance;

    @Override
    public void visit(ATM atm) {
        accumulatedBalance += atm.getATMBalance();
    }

}
