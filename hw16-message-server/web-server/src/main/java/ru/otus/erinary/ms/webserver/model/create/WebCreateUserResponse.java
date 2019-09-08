package ru.otus.erinary.ms.webserver.model.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.webserver.model.WebMessage;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WebCreateUserResponse extends WebMessage {

    private String status;
    private String message;

}
