package ru.otus.erinary.ms.messageserver.model;

import lombok.Data;

import java.util.Set;

@Data
public class User {

    private String name;
    private int age;
    private Address address;
    private Set<Phone> phones;

}
