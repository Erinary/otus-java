package ru.otus.erinary.h2;

import ru.otus.erinary.model.User;

import java.sql.*;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
public class H2DataBase {

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
            statement.execute("CREATE TABLE USER(id BIGINT(20) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age INT(3))");
            connection.commit();
        }
    }

    public void insertUser(String name, int age) throws SQLException {
        try (PreparedStatement pstatement = connection.prepareStatement("INSERT INTO USER(name, age) VALUES (?, ?)")) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            pstatement.setString(1, name);
            pstatement.setInt(2, age);
            try {
                pstatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                e.printStackTrace();
            }
        }
    }

    public User selectUserById(long id) throws SQLException {
        User result = null;
        try (PreparedStatement pstatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?")) {
            pstatement.setLong(1, id);
            try (ResultSet resultSet = pstatement.executeQuery()) {
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

    public void close() throws SQLException {
        connection.close();
    }
}
