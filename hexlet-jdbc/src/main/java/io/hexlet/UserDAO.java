package io.hexlet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class UserDAO {
    private Connection connection;

    public UserDAO(Connection conn) {
        connection = conn;
    }

    public void save(User user) throws SQLException {
        if (user.isNew()) {
            addNewUser(user);
        } else {
            updateUser(user);
        }
    }

    // Возвращается Optional<User>
    // Это упрощает обработку ситуаций, когда в базе ничего не найдено
    public Optional<User> find(Long id) throws SQLException {
        var sql = "SELECT * FROM users WHERE id = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var username = resultSet.getString("username");
                var phone = resultSet.getString("phone");
                var user = new User(username, phone);
                user.setId(id);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }

    public void createTableUser() throws SQLException {
        var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), "
                + "phone VARCHAR(255))";
        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public List<User> getUsers(String sqlQuery) throws SQLException {
        /*List<User> listUsers = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                addUserToList(listUsers, resultSet);
            }

            return listUsers;
        }*/

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        return getUsers(statement);
    }

    public List<User> getUsers(PreparedStatement preparedStatement) throws SQLException {
        List<User> listUsers = new ArrayList<>();
        try (preparedStatement) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                addUserToList(listUsers, resultSet);
            }

            return listUsers;
        }
    }

    public void delete(User user) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try(var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.executeUpdate();
        }
    }

    private void addUserToList(List<User> list, ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String phone = resultSet.getString("phone");

        list.add(new User(id, username, phone));
    }

    private void addNewUser(User user) throws SQLException {
        var sql = "INSERT INTO users (username, phone) VALUES (?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhone());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            // Если идентификатор сгенерирован, извлекаем его и добавляем в сохраненный объект
            if (generatedKeys.next()) {
                // Обязательно устанавливаем id в сохраненный объект
                user.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    private void updateUser(User user) throws SQLException {
        var sql = "UPDATE users SET username = ?, phone = ? WHERE id = ?";
        try (var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhone());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
        }
    }
}
