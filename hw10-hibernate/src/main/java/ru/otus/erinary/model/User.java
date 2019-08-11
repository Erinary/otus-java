package ru.otus.erinary.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    private Set<Phone> phones;

    public User(String name, int age, Address address, Set<Phone> phones) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = new HashSet<>();
        if (phones != null) {
            phones.forEach(this::addPhone);
        }
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
        phone.setUser(this);
    }
}
