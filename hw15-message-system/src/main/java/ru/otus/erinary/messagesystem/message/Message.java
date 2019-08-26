package ru.otus.erinary.messagesystem.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

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
public abstract class Message {

    private String sessionId;

}
