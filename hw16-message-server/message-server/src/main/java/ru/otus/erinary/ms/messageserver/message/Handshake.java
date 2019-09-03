package ru.otus.erinary.ms.messageserver.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Handshake implements Serializable {

    private static final long serialVersionUID = 1L;
    private String getFromQueue;

}
