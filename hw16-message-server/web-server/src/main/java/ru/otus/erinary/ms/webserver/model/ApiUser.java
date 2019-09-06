package ru.otus.erinary.ms.webserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.erinary.ms.messageserver.model.Address;
import ru.otus.erinary.ms.messageserver.model.Phone;
import ru.otus.erinary.ms.messageserver.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ApiUser {

    private String userName;
    private int userAge;
    private String userAddress;
    private List<String> userPhones;

    public ApiUser(User user) {
        this.userName = user.getName();
        this.userAge = user.getAge();
        this.userAddress = user.getAddress().getStreet();
        this.userPhones = user.getPhones().stream().map(Phone::getNumber).collect(Collectors.toList());
    }

    public User toUser() {
        return new User(
                userName,
                userAge,
                new Address(userAddress),
                userPhones.stream().map(Phone::new).collect(Collectors.toSet())
        );
    }

}
