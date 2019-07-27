package ru.otus.erinary.h2;

import ru.otus.erinary.model.User;

import java.sql.*;

class H2DataBase {

    private static final String URL = "jdbc:h2:mem:";
    private final Connection connection;

    H2DataBase() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
        this.connection.setAutoCommit(false);
    }

    void createUserTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE USER(id BIGINT(20), name VARCHAR(255), age INT(3))");
        }
    }

    void insertUser(long id, String name, int age) throws SQLException {
        try (PreparedStatement pstatement = connection.prepareStatement("INSERT INTO USER(id, name, age) VALUES (?,?,?)")) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            pstatement.setLong(1, id);
            pstatement.setString(2, name);
            pstatement.setInt(3, age);
            try {
                pstatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                e.printStackTrace();
            }
        }
    }

    User selectUserById(long id) throws SQLException {
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

    void close() throws SQLException {
        connection.close();
    }
}
