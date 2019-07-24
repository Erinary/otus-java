package ru.otus.erinary.atm;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.erinary.atm.exception.ATMServiceException;

@Data
@AllArgsConstructor
public class Cell {

    private final Denomination denomination;
    private long amount;

    @SuppressWarnings("WeakerAccess")
    public void addBankNotes(long number) {
        amount += number;
    }

    public void removeBankNotes(long number) {
        if (number > amount) {
            throw new ATMServiceException("В ячейке [" + denomination.value + "] кончились банкноты");
        }
        amount -= number;
    }

}
