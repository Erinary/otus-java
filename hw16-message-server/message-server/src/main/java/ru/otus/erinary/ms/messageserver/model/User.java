package ru.otus.erinary.ms.messageserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private Address address;
    private Set<Phone> phones;

}
