package ru.otus.erinary.h2;

import ru.otus.erinary.model.Address;
import ru.otus.erinary.model.User;

import java.sql.*;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
public class H2DataBase {

    private static final String INSERT_USER = "INSERT INTO users(id, name, age) VALUES (?, ?, ?)";
    private static final String SELECT_USER = "SELECT * FROM users WHERE id = ?";

    private static final String INSERT_ADDRESS = "INSERT INTO addresses(id, street) VALUES(?, ?)";
    private static final String SELECT_ADDRESS = "SELECT * FROM addresses WHERE id = ?";

    private static final String INSERT_PHONE = "INSERT INTO phones(id, phone_number, user_id) VALUES(?, ?, ?)";
    private static final String SELECT_PHONE = "SELECT * FROM phones WHERE id = ?";
    private final Connection connection;

    public H2DataBase(String URL) throws SQLException {
        this.connection = DriverManager.getConnection(URL);
        this.connection.setAutoCommit(false);
    }

    public void insertUser(long id, String name, int age) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            statement.setLong(1, id);
            statement.setString(2, name);
            statement.setInt(3, age);
            try {
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                e.printStackTrace();
            }
        }
    }

    public User selectUserById(long id) throws SQLException {
        User result = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_USER)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new User(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("age"),
                            null
//                            null
                    );
                }
            }
        }
        return result;
    }

    public void insertAddress(long id, String street) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_ADDRESS)) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            statement.setLong(1, id);
            statement.setString(2, street);
            try {
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                e.printStackTrace();
            }
        }
    }

    public Address selectAddressById(long id) throws SQLException {
        Address result = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ADDRESS)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new Address(
                            resultSet.getLong("id"),
                            resultSet.getString("street")
                    );
                }
            }
        }
        return result;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
