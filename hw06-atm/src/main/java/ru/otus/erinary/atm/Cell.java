package ru.otus.erinary.atm;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.erinary.atm.exception.ATMServiceException;

@Data
@AllArgsConstructor
public class Cell {

    private final Denomination denomination;
    private long amount;

    public void addBankNotes(long number) {
        amount += number;
    }

    public void removeBankNotes(long number) {
        if (number > amount) {
            throw new ATMServiceException("В ячейке [" + denomination.value + "] кончились банкноты");
        }
        amount -= number;
    }

    public enum Denomination {
        HUNDRED(100),
        FIVE_HUNDRED(500),
        THOUSAND(1000),
        FIVE_THOUSAND(5000);

        public final int value;

        Denomination(int value) {
            this.value = value;
        }
    }
}
