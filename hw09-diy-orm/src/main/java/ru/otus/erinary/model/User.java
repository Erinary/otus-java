package ru.otus.erinary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.erinary.annotation.Id;

@Data
@AllArgsConstructor
public class User {

    @Id
    private long id;
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
