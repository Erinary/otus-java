package ru.otus.erinary.ms.messageserver.message.create;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.messageserver.message.Message;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class CreateUserResponse extends Message {

    private String status;
    private String message;

}
