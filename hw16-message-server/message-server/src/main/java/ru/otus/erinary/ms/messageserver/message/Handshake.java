package ru.otus.erinary.ms.messageserver.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Класс "рукопожатия" между клиент-сокетом и сервер-сокетом
 */
@Data
@AllArgsConstructor
public class Handshake implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Имя очереди, из которой сервис будет принимать сообщения */
    private String getFromQueue;

}
