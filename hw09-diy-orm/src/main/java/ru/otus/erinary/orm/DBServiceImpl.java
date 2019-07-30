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
    private final Class<T> tClass;

    public DBServiceImpl(Connection connection, Class<T> tClass) {
        String tableName = tClass.getSimpleName();
        this.connection = connection;
        this.tClass = tClass;
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
        } catch (SQLException | IllegalAccessException e) {
            System.out.println("Failed to insert entity into database");
            throw new DBServiceException(e.getMessage(), e);
        }
        System.out.println("ObjectData was successfully inserted");
    }

    @Override
    public void update(T objectData) {
        try {
            idField.setAccessible(true);
            long id = (Long) idField.get(objectData);
            if (load(id) != null) {
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
            } else {
                throw new DBServiceException("Entity doesn't exist in database");
            }
        } catch (IllegalAccessException e) {
            System.out.println("Error while preforming request");
            throw new DBServiceException(e.getMessage(), e);
        } finally {
            idField.setAccessible(false);
        }
        System.out.println("ObjectData was successfully updated");
    }

    @Override
    public void createOrUpdate(T objectData) {
        try {
            idField.setAccessible(true);
            long id = (Long) idField.get(objectData);
            if (load(id) != null) {
                update(objectData);
            } else {
                create(objectData);
            }
        } catch (IllegalAccessException e) {
            System.out.println("Error while preforming request");
            throw new DBServiceException(e.getMessage(), e);
        } finally {
            idField.setAccessible(false);
        }
    }

    @Override
    public T load(long id) {
        T result = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {
            statement.setObject(1, id);
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    result = tClass.getConstructor().newInstance();
                    for (Field field : tClass.getDeclaredFields()) {
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
