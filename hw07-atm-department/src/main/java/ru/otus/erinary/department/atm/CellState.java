package ru.otus.erinary.department.atm;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("WeakerAccess")
@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CellState {

    private final Denomination denomination;
    private final long amount;

}
