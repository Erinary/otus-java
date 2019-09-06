package ru.otus.erinary.ms.messageserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    private String number;
    private User user;

    public Phone(String number) {
        this.number = number;
    }

}
