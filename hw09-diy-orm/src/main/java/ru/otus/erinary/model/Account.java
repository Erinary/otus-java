package ru.otus.erinary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.erinary.annotation.Id;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Account {

    @Id
    private long id;
    private String type;
    private BigDecimal rest;

    public Account() {}

    public Account(String type, BigDecimal rest) {
        this.type = type;
        this.rest = rest;
    }
}
