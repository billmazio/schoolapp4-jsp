package gr.aueb.cf.schoolapp.authentication;

import gr.aueb.cf.schoolapp.dao.UserDAO;
import gr.aueb.cf.schoolapp.dao.UserDAOImpl;
import gr.aueb.cf.schoolapp.dao.exceptions.UserDAOException;
import gr.aueb.cf.schoolapp.dto.UserLoginDTO;
import gr.aueb.cf.schoolapp.model.User;

public class AuthenticationProvider {

    private static final UserDAO userDAO = new UserDAOImpl();

    private AuthenticationProvider() {}

    public static boolean authenticate(UserLoginDTO userLoginDTO) {
        // Validate authentication
        try {
            return userDAO.isUserValid(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        } catch (UserDAOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean register(UserLoginDTO userLoginDTO) {
        // Create a new User and register it
        User newUser = new User(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        try {
            return userDAO.registerUser(newUser);
        } catch (UserDAOException e) {
            throw new RuntimeException(e);
        }
    }
}
