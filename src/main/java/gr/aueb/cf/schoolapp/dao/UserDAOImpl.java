package gr.aueb.cf.schoolapp.dao;

import gr.aueb.cf.schoolapp.dao.exceptions.UserDAOException;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.security.SecUtil;

import java.sql.*;

import static gr.aueb.cf.schoolapp.service.util.DBUtil.getConnection;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean isUserValid(String username, String password) throws UserDAOException {
        // Implement your query to validate user here
        String sql = "SELECT password FROM users WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");
                return SecUtil.checkPassword(password, storedHashedPassword);
            }

        } catch (SQLException e) {
            throw new UserDAOException("Error while checking user validity.", e);
        }
        return false;
    }

    @Override
    public boolean registerUser(User user) throws UserDAOException {
        // Implement your query to insert new user into database here
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            int affectedRows = statement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            throw new UserDAOException("Error while registering user.", e);
        }
    }

    @Override
    public boolean isUserExists(String username) throws UserDAOException {
        // SQL query to check if a user with the provided username exists
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            // Set the value of the username parameter
            statement.setString(1, username);

            // Execute the query
            ResultSet rs = statement.executeQuery();

            // If the query returned a result
            if (rs.next()) {
                // If the count is greater than 0, the user exists
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new UserDAOException("Error while checking user existence.", e);
        }

        // If we reach this point, an error occurred or the user does not exist
        return false;
    }
}
