package ru.otus.erinary.ms.messageserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    private String street;

}
