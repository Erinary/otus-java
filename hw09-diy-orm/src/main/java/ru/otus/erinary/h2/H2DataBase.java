package ru.otus.erinary.h2;

import ru.otus.erinary.model.Account;
import ru.otus.erinary.model.User;

import java.math.BigDecimal;
import java.sql.*;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
public class H2DataBase {

    private static final String CREATE_USER_TABLE = "CREATE TABLE user(id BIGINT(20) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age INT(3))";
    private static final String INSERT_USER = "INSERT INTO user(name, age) VALUES (?, ?)";
    private static final String SELECT_USER = "SELECT * FROM user WHERE id = ?";
    private static final String CREATE_ACCOUNT_TABLE = "CREATE TABLE account(id BIGINT(20) AUTO_INCREMENT PRIMARY KEY, type VARCHAR(255), rest NUMBER)";
    private static final String INSERT_ACCOUNT = "INSERT INTO account(type, rest) VALUES (?, ?)";
    private static final String SELECT_ACCOUNT = "SELECT * FROM account WHERE id = ?";

    private static final String URL = "jdbc:h2:mem:";
    private final Connection connection;

    public H2DataBase() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
        this.connection.setAutoCommit(false);
    }

    public Connection getConnection() {
        return connection;
    }

    public void createUserTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_USER_TABLE);
            connection.commit();
        }
    }

    public void insertUser(String name, int age) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            statement.setString(1, name);
            statement.setInt(2, age);
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
                            resultSet.getInt("age")
                    );
                }
            }
        }
        return result;
    }

    public void createAccountTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_ACCOUNT_TABLE);
            connection.commit();
        }
    }

    public void insertAccount(String type, BigDecimal rest) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT)) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            statement.setString(1, type);
            statement.setBigDecimal(2, rest);
            try {
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                e.printStackTrace();
            }
        }
    }

    public Account selectAccountById(long id) throws SQLException {
        Account result = null;
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new Account(
                            resultSet.getLong("id"),
                            resultSet.getString("type"),
                            resultSet.getBigDecimal("rest")
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
