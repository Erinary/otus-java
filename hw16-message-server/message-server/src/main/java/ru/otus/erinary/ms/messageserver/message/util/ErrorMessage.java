package ru.otus.erinary.ms.messageserver.message.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.messageserver.message.Message;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErrorMessage extends Message {

    private String message;

}
