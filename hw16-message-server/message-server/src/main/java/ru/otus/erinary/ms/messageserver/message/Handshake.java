package ru.otus.erinary.ms.messageserver.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class Handshake implements Serializable {

    private static final long serialVersionUID = 1L;
    private String getFromQueue;

}
