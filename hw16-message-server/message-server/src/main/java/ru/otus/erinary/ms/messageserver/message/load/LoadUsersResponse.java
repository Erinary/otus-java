package ru.otus.erinary.ms.messageserver.message.load;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.messageserver.message.Message;
import ru.otus.erinary.ms.messageserver.model.User;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoadUsersResponse extends Message {

    private List<User> users;

}
