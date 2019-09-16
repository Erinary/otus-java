package ru.otus.erinary.ms.webserver.model.load;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.messageserver.message.Message;
import ru.otus.erinary.ms.messageserver.model.User;
import ru.otus.erinary.ms.webserver.model.WebMessage;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WebLoadUsersResponse extends WebMessage {

    private List<User> users;

}
