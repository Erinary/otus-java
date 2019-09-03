package ru.otus.erinary.messagesystem.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErrorMessage extends Message {

    private String message;

}
