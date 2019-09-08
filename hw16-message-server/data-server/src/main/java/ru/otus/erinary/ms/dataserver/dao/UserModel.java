package ru.otus.erinary.ms.dataserver.dao;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.erinary.ms.messageserver.model.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AddressModel address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    private Set<PhoneModel> phones;

    public UserModel(String name, int age, AddressModel address, Set<PhoneModel> phones) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = new HashSet<>();
        if (phones != null) {
            phones.forEach(this::addPhone);
        }
    }

    public UserModel(User user) {
        this.name = user.getName();
        this.age = user.getAge();
        this.address = new AddressModel(user.getAddress());
        this.phones = new HashSet<>();
        if (user.getPhones() != null) {
            user.getPhones().forEach(phone -> addPhone(new PhoneModel(phone)));
        }
    }

    public User toUser() {
        return new User(name, age, address.toAddress(), phones.stream().map(PhoneModel::toPhone).collect(Collectors.toSet()));
    }

    private void addPhone(PhoneModel phone) {
        phones.add(phone);
        phone.setUser(this);
    }

    @PrePersist
    @PreUpdate
    private void ensurePhonesUser() {
        phones.forEach(phone -> phone.setUser(this));
    }

}
