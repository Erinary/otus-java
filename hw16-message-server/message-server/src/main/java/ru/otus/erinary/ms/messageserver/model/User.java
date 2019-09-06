package ru.otus.erinary.ms.messageserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class User {

    private String name;
    private int age;
    private Address address;
    private Set<Phone> phones;

}
