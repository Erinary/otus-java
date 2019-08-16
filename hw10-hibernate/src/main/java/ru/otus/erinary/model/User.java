package ru.otus.erinary.model;

import com.google.gson.annotations.Expose;
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
    @Column(name = "id")
    @Expose
    private long id;

    @Column(name = "name")
    @Expose
    private String name;

    @Column(name = "age")
    @Expose
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Expose
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    @Expose
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

    @PrePersist
    @PreUpdate
    private void ensurePhonesUser() {
        phones.forEach(phone -> phone.setUser(this));
    }
}
