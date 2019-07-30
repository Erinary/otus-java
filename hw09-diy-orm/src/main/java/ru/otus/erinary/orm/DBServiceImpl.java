package ru.otus.erinary.orm;

import ru.otus.erinary.annotation.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            fillStatementWithFieldValues(statement, objectData);
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
            System.out.println("Failed to insert entity into database");
            throw new DBServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void update(T objectData) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            fillStatementWithFieldValues(statement, objectData);
            try {
                idField.setAccessible(true);
                statement.setObject(classFields.size() + 1, idField.get(objectData));
            } finally {
                idField.setAccessible(false);
            }
            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            System.out.println("Failed to update entity in database");
            throw new DBServiceException(e.getMessage(), e);
        }
    }

    private void fillStatementWithFieldValues(PreparedStatement statement, T objectData) throws IllegalAccessException, SQLException {
        for (int i = 1; i <= classFields.size(); ++i) {
            Field field = classFields.get(i - 1);
            try {
                field.setAccessible(true);
                statement.setObject(i, field.get(objectData));
            } finally {
                field.setAccessible(false);
            }
        }
    }

    @Override
    public void createOrUpdate(T objectData) {

    }

    @Override
    public T load(long id, Class<T> t1Class) {
        T result = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setObject(1, id);
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    result = t1Class.getConstructor().newInstance();
                    for (Field field : t1Class.getDeclaredFields()) {
                        try {
                            field.setAccessible(true);
                            field.set(result, resultSet.getObject(field.getName()));
                        } finally {
                            field.setAccessible(false);
                        }
                    }
                }
            } catch (InvocationTargetException | InstantiationException e) {
                System.out.println("Failed to create new entity");
                throw new DBServiceException(e.getMessage(), e);
            }
        } catch (SQLException | IllegalAccessException | NoSuchMethodException e) {
            System.out.println("Failed to select entity from database");
            throw new DBServiceException(e.getMessage(), e);
        }
        return result;
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
        return "UPDATE " + tableName + " SET " + classFields.stream().
                map(field -> field.getName() + " = ?").collect(Collectors.joining(", "))
                + " WHERE " + idField.getName() + " = ?";
    }

    private String prepareSelectQuery(String tableName) {
        return "SELECT " + Stream.concat(Stream.of(idField), classFields.stream())
                .map(Field::getName).collect(Collectors.joining(", ")) +
                " FROM " + tableName + " WHERE " + idField.getName() + " = ?";
    }
}
