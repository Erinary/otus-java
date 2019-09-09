package ru.otus.erinary.ms.messageserver.message;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Очередь, в которую нужно поместить запрос */
    private String putToQueue;

    /** Очередь, в которую нужно вернуть ответ */
    private String replyTo;

    /** Id сессии веб-сокета */
    private String webSocketSessionId;

}
