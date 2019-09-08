package ru.otus.erinary.ms.webserver.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import ru.otus.erinary.ms.webserver.model.create.WebCreateUserRequest;
import ru.otus.erinary.ms.webserver.model.create.WebCreateUserResponse;
import ru.otus.erinary.ms.webserver.model.load.WebLoadUsersRequest;
import ru.otus.erinary.ms.webserver.model.load.WebLoadUsersResponse;
import ru.otus.erinary.ms.webserver.model.util.WebErrorMessage;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WebCreateUserRequest.class, name = "createUserRequest"),
        @JsonSubTypes.Type(value = WebCreateUserResponse.class, name = "createUserResponse"),
        @JsonSubTypes.Type(value = WebLoadUsersRequest.class, name = "loadUsersRequest"),
        @JsonSubTypes.Type(value = WebLoadUsersResponse.class, name = "loadUsersResponse"),
        @JsonSubTypes.Type(value = WebErrorMessage.class, name = "errorResponse")
})
@Data
public abstract class WebMessage {
}
