package ru.otus.erinary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int age;

//    @OneToOne
//    @PrimaryKeyJoinColumn
//    private Address address;

//    @OneToMany
//    private Set<Phone> phones;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
