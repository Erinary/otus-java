package ru.otus.erinary.orm;

import ru.otus.erinary.annotation.Id;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DBServiceImpl<T> implements DBService<T> {

    private final String INSERT_QUERY;
    private final String UPDATE_QUERY;
    private final String SELECT_QUERY;

    private final Connection connection;
    private final Field idField;
    private final List<Field> classFields;

    public DBServiceImpl(Connection connection, Class<T> tClass) {
        String tableName = tClass.getSimpleName();
        this.connection = connection;
        this.idField = getClassIdField(tClass);
        this.classFields = getClassFields(tClass);
        this.INSERT_QUERY = prepareInsertQuery(tableName);
        this.UPDATE_QUERY = prepareUpdateQuery(tableName);
        this.SELECT_QUERY = prepareSelectQuery(tClass.getSimpleName());
    }

    @Override
    public void create(T objectData) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 1; i <= classFields.size(); ++i) {
                Field field = classFields.get(i - 1);
                try {
                    field.setAccessible(true);
                    statement.setObject(i, field.get(objectData));
                } finally {
                    field.setAccessible(false);
                }
            }
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    try {
                        idField.setAccessible(true);
                        idField.set(objectData, generatedKeys.getObject(1));
                    } finally {
                        idField.setAccessible(false);
                    }
                }
            }
            System.out.println("ObjectData was successfully inserted");
        } catch (SQLException | IllegalAccessException e) {
            System.out.println("Failed to insert into DB");
            throw new DBServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void update(T objectData) {

    }

    @Override
    public void createOrUpdate(T objectData) {

    }

    @Override
    public T load(long id, Class<T> t1Class) {
        return null;
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

    private String prepareInsertQuery(String tableName) {
        return "INSERT INTO " + tableName + "(" +
                classFields.stream().map(Field::getName).collect(Collectors.joining(",")) +
                ")" + " VALUES (" + String.join(",", Collections.nCopies(classFields.size(), "?")) + ")";
    }

    private String prepareUpdateQuery(String tableName) {
        return tableName;
    }

    private String prepareSelectQuery(String tableName) {
        return tableName;
    }
}
