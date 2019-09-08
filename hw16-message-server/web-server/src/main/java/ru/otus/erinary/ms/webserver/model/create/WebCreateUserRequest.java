package ru.otus.erinary.ms.webserver.model.create;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.webserver.model.ApiUser;
import ru.otus.erinary.ms.webserver.model.WebMessage;

@Data
@EqualsAndHashCode(callSuper = true)
public class WebCreateUserRequest extends WebMessage {

    private ApiUser apiUser;

}
