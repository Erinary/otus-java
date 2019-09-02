package ru.otus.erinary.department;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.department.atm.ATM;
import ru.otus.erinary.department.payment.DefaultStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentTest {

    private Department department;

    @BeforeEach
    void setup() {
        department = new Department();
    }

    @Test
    void testRestoreATMState() {
        String id = department.createDefaultATM();
        assertEquals(66000, department.getAllATMBalance());
        ATM atm = department.getATM(id);
        atm.getMoney(new DefaultStrategy(), 12700);
        assertEquals(53300, department.getAllATMBalance());
        department.restoreAllATM();
        assertEquals(66000, department.getAllATMBalance());
    }

    @Test
    void testGetAllATMBalance() {
        department.createDefaultATM();
        assertEquals(66000, department.getAllATMBalance());
        department.createDefaultATM();
        assertEquals(132000, department.getAllATMBalance());

    }
}