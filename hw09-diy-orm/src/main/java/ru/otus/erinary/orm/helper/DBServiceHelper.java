package ru.otus.erinary.orm.helper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DBServiceHelper {

    public static <T> String prepareInsertQuery(String tableName, ClassMetaData<T> metaData) {
        return "INSERT INTO " + tableName + "(" +
                metaData.getClassFields().stream().map(Field::getName).collect(Collectors.joining(",")) +
                ")" + " VALUES (" + String.join(",", Collections.nCopies(metaData.getClassFields().size(), "?")) + ")";
    }

    public static <T> String prepareUpdateQuery(String tableName, ClassMetaData<T> metaData) {
        return "UPDATE " + tableName + " SET " + metaData.getClassFields().stream().
                map(field -> field.getName() + " = ?").collect(Collectors.joining(", "))
                + " WHERE " + metaData.getIdField().getName() + " = ?";
    }

    public static <T> String prepareSelectQuery(String tableName, ClassMetaData<T> metaData) {
        return "SELECT " + Stream.concat(Stream.of(metaData.getIdField()), metaData.getClassFields().stream())
                .map(Field::getName).collect(Collectors.joining(", ")) +
                " FROM " + tableName + " WHERE " + metaData.getIdField().getName() + " = ?";
    }

    public static <T> String prepareExistQuery(String tableName, ClassMetaData<T> metaData) {
        return "SELECT 1 FROM " + tableName + " WHERE " + metaData.getIdField().getName() + " = ?";
    }

}
