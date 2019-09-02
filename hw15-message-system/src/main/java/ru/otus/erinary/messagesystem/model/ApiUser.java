package ru.otus.erinary.messagesystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.erinary.model.Address;
import ru.otus.erinary.model.Phone;
import ru.otus.erinary.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ApiUser {

    private String name;
    private int age;
    private String address;
    private List<String> phones;

    public ApiUser(User user) {
        this.name = user.getName();
        this.age = user.getAge();
        this.address = user.getAddress().getStreet();
        this.phones = user.getPhones().stream().map(Phone::getNumber).collect(Collectors.toList());
    }

    public User toUser() {
        return new User(
                name,
                age,
                new Address(address),
                phones.stream().map(Phone::new).collect(Collectors.toSet())
        );
    }

}
