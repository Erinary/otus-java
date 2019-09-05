package ru.otus.erinary.ms.messageserver.model;

import lombok.Data;

@Data
public class Phone {

    private String number;
    private User user;

}
