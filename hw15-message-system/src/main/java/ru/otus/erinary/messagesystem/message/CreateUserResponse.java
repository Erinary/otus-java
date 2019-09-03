package ru.otus.erinary.messagesystem.message;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class CreateUserResponse extends Message {

    private String status;
    private String message;

}
