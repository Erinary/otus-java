package ru.otus.erinary.ms.webserver.model.load;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.webserver.model.WebMessage;

@Data
@EqualsAndHashCode(callSuper = true)
public class WebLoadUsersRequest extends WebMessage {
}
