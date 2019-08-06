package ru.otus.erinary.orm;

import ru.otus.erinary.orm.helper.ClassMetaData;
import ru.otus.erinary.orm.helper.DBServiceException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

import static ru.otus.erinary.orm.helper.DBServiceHelper.*;

public class DBServiceImpl<T> implements DBService<T> {

    private final String INSERT_QUERY;
    private final String UPDATE_QUERY;
    private final String SELECT_QUERY;

    private final Connection connection;
    private final ClassMetaData<T> metaData;

    public DBServiceImpl(Connection connection, Class<T> tClass) {
        String tableName = tClass.getSimpleName();
        this.connection = connection;
        this.metaData = new ClassMetaData<>(tClass);
        this.INSERT_QUERY = prepareInsertQuery(tableName, metaData);
        this.UPDATE_QUERY = prepareUpdateQuery(tableName, metaData);
        this.SELECT_QUERY = prepareSelectQuery(tClass.getSimpleName(), metaData);
    }

    @Override
    public void create(T objectData) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            fillStatementWithFieldValues(statement, objectData);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    try {
                        metaData.getIdField().setAccessible(true);
                        metaData.getIdField().set(objectData, generatedKeys.getObject(1));
                    } finally {
                        metaData.getIdField().setAccessible(false);
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
            metaData.getIdField().setAccessible(true);
            long id = (Long) metaData.getIdField().get(objectData);
            if (load(id) != null) {
                try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
                    fillStatementWithFieldValues(statement, objectData);
                    try {
                        metaData.getIdField().setAccessible(true);
                        statement.setObject(metaData.getClassFields().size() + 1,
                                metaData.getIdField().get(objectData));
                    } finally {
                        metaData.getIdField().setAccessible(false);
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
            metaData.getIdField().setAccessible(false);
        }
        System.out.println("ObjectData was successfully updated");
    }

    @Override
    public void createOrUpdate(T objectData) {
        try {
            metaData.getIdField().setAccessible(true);
            long id = (Long) metaData.getIdField().get(objectData);
            if (load(id) != null) {
                update(objectData);
            } else {
                create(objectData);
            }
        } catch (IllegalAccessException e) {
            System.out.println("Error while preforming request");
            throw new DBServiceException(e.getMessage(), e);
        } finally {
            metaData.getIdField().setAccessible(false);
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
                    result = metaData.getTClass().getConstructor().newInstance();
                    for (Field field : metaData.getTClass().getDeclaredFields()) {
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
        for (int i = 1; i <= metaData.getClassFields().size(); ++i) {
            Field field = metaData.getClassFields().get(i - 1);
            try {
                field.setAccessible(true);
                statement.setObject(i, field.get(objectData));
            } finally {
                field.setAccessible(false);
            }
        }
    }
}
