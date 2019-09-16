package ru.otus.erinary.ms.messageserver.message.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.messageserver.message.Message;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateUserResponse extends Message {

    private String status;
    private String message;

}
