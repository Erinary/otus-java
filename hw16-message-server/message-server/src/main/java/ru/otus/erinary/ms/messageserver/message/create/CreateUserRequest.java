package ru.otus.erinary.ms.messageserver.message.create;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.messageserver.message.Message;
import ru.otus.erinary.ms.messageserver.model.User;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateUserRequest extends Message {

    private User user;

}
