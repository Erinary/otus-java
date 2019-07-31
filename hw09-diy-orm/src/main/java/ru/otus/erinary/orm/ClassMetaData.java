package ru.otus.erinary.orm;

import lombok.Getter;
import ru.otus.erinary.annotation.Id;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ClassMetaData<T> {

    private final Field idField;
    private final List<Field> classFields;
    private final Class<T> tClass;

    public ClassMetaData(Class<T> tClass) {
        this.idField = getClassIdField(tClass);
        this.classFields = getClassFields(tClass);
        this.tClass = tClass;
    }

    private Field getClassIdField(Class<T> tClass) {
        for (Field field : tClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new DBServiceException("No '@Id' annotation for class");
    }

    private List<Field> getClassFields(Class<T> tClass) {
        return Arrays.stream(tClass.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }
}
