package ru.otus.erinary.messagesystem.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.messagesystem.model.ApiUser;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoadUsersResponse extends Message {

    private List<ApiUser> users;

}
