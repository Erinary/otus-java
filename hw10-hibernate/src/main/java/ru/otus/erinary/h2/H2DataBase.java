package ru.otus.erinary.h2;

import ru.otus.erinary.model.User;

import java.sql.*;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
public class H2DataBase {

    private static final String CREATE_TABLES = "CREATE TABLE users(id BIGINT(20) AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(255) NOT NULL, age INT(3) NOT NULL); " +
            "CREATE TABLE addresses(id BIGINT(20) AUTO_INCREMENT PRIMARY KEY, " +
            "street VARCHAR(255) NOT NULL); " +
            "CREATE TABLE phones(id BIGINT(20) AUTO_INCREMENT PRIMARY KEY, " +
            "phone_number VARCHAR(255), user_id BIGINT(20), FOREIGN KEY (user_id) REFERENCES users(id))";

    private static final String INSERT_USER = "INSERT INTO users(name, age) VALUES (?, ?)";
    private static final String SELECT_USER = "SELECT * FROM users WHERE id = ?";

    private static final String INSERT_ADDRESS = "INSERT INTO addresses(street) VALUES(?)";
    private static final String SELECT_ADDRESS = "SELECT * FROM addresses WHERE id = ?";

    private static final String INSERT_PHONE = "INSERT INTO phones(phone_number, user_id) VALUES(?, ?)";
    private static final String SELECT_PHONE = "SELECT * FROM phones WHERE id = ?";


    private static final String URL = "jdbc:h2:mem:";
    private final Connection connection;

    public H2DataBase() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
        this.connection.setAutoCommit(false);
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLES);
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
                            resultSet.getInt("age"),
                            null,
                            null
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
