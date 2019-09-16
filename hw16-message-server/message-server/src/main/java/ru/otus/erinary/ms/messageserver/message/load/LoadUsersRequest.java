package ru.otus.erinary.ms.messageserver.message.load;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.erinary.ms.messageserver.message.Message;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoadUsersRequest extends Message {
}
