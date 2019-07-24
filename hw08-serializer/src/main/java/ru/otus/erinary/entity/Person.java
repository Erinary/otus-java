package ru.otus.erinary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Person {

    private String name;
    private int age;
    private String city;
    private List<String> pets;
    private String[] hobbies;

}
