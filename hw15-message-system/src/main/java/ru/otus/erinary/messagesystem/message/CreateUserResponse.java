package ru.otus.erinary.messagesystem.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateUserResponse extends Message {

    private String status;
    private String response;

}
