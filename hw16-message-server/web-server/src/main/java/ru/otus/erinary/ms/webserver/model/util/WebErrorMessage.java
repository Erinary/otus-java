package ru.otus.erinary.ms.webserver.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.messageserver.message.Message;
import ru.otus.erinary.ms.webserver.model.WebMessage;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WebErrorMessage extends WebMessage {

    private String message;

}
