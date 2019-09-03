package ru.otus.erinary.messagesystem.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.messagesystem.model.ApiUser;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateUserRequest extends Message {

    private ApiUser data;

}
