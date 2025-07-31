package io.hexlet;

import java.sql.SQLException;
import java.sql.DriverManager;

public class Application {

    public static void main(String[] args) throws SQLException {

        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            UserDAO userDAO = new UserDAO(conn);

            userDAO.createTableUser();

            User newUser = new User("tommy", "123456789");
            userDAO.save(newUser);

            String sql = "SELECT * FROM users";
            var listUsers = userDAO.getUsers(sql);
            for (User user : listUsers) {
                System.out.printf("id = %s, username = %s, phone = %s\n",
                        user.getId(),
                        user.getName(),
                        user.getPhone()
                );
            }
        }
    }
}
