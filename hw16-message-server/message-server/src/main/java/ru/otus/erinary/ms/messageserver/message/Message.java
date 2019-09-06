package ru.otus.erinary.ms.messageserver.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import ru.otus.erinary.ms.messageserver.message.create.CreateUserRequest;
import ru.otus.erinary.ms.messageserver.message.create.CreateUserResponse;
import ru.otus.erinary.ms.messageserver.message.load.LoadUsersRequest;
import ru.otus.erinary.ms.messageserver.message.load.LoadUsersResponse;
import ru.otus.erinary.ms.messageserver.message.util.ErrorMessage;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateUserRequest.class, name = "createUserRequest"),
        @JsonSubTypes.Type(value = CreateUserResponse.class, name = "createUserResponse"),
        @JsonSubTypes.Type(value = LoadUsersRequest.class, name = "loadUsersRequest"),
        @JsonSubTypes.Type(value = LoadUsersResponse.class, name = "loadUsersResponse"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "errorResponse")
})
@Data
public abstract class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private String putToQueue;
    private String replyTo;
    private String webSocketSessionId;

}
