package ru.otus.erinary.ms.messageserver.message.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.messageserver.message.Message;
import ru.otus.erinary.ms.messageserver.model.User;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateUserRequest extends Message {

    private User user;

}
