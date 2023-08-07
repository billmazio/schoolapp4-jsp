//package gr.aueb.cf.schoolapp.service;
//
//import gr.aueb.cf.schoolapp.dao.UserDAO;
//import gr.aueb.cf.schoolapp.dao.UserDAOImpl;
//import gr.aueb.cf.schoolapp.dto.UserDTO;
//
//import static gr.aueb.cf.schoolapp.security.SecUtil.hashPassword;
//
//import static gr.aueb.cf.schoolapp.security.SecUtil.hashPassword;
//
//public class UserServiceImpl implements UserService {
//
//    private UserDAO userDAO;
//
//    public UserServiceImpl() {
//        this.userDAO = new UserDAOImpl();
//    }
//
//    @Override
//    public boolean registerUser(UserDTO userDTO) {
//        // Validate input
//        if (!isInputValid(userDTO)) {
//            return false;
//        }
//
//        // Check if username already exists
//        if (userDAO.isUserExists(userDTO.getUsername())) {
//            return false;
//        }
//
//        // Hash the user's password for secure storage
//        String hashedPassword = hashPassword(userDTO.getPassword());
//        userDTO.setPassword(hashedPassword);
//
//        // Register the user using the DAO
//        return userDAO.registerUser(userDTO);
//    }
//
//    @Override
//    public boolean isUserValid(String username, String password) {
//        // Validate input
//        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
//            return false;
//        }
//
//        // Hash the provided password with the same method used during registration
//       //   String hashedPassword = hashPassword(password);
//
//        // Validate the user using the DAO
//        return userDAO.isUserValid(username, password);
//    }
//
//    private boolean isInputValid(UserDTO userDTO) {
//        // Add your validation logic here
//        // This is a very basic example: check that the username and password are not null or empty
//        return userDTO.getUsername() != null && !userDTO.getUsername().trim().isEmpty()
//                && userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty();
//    }
//}
package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dao.UserDAO;
import gr.aueb.cf.schoolapp.dao.UserDAOImpl;
import gr.aueb.cf.schoolapp.dto.UserDTO;
import gr.aueb.cf.schoolapp.dao.exceptions.UserDAOException;
import gr.aueb.cf.schoolapp.service.exceptions.UserNotFoundException;

import static gr.aueb.cf.schoolapp.security.SecUtil.hashPassword;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }


    @Override
    public boolean registerUser(UserDTO userDTO) throws UserNotFoundException, UserDAOException {
        // Validate input
        if (!isInputValid(userDTO)) {
            throw new UserNotFoundException("Invalid user input.");
        }

        // Check if username already exists
        if (userDAO.isUserExists(userDTO.getUsername())) {
            throw new UserNotFoundException("Username already exists. Please choose a different username.");
        }

        // Hash the user's password for secure storage
        String hashedPassword = hashPassword(userDTO.getPassword());
        userDTO.setPassword(hashedPassword);

        // Register the user using the DAO
        try {
            return userDAO.registerUser(userDTO);
        } catch (UserDAOException e) {
            throw new UserNotFoundException("Error while registering user.", e);
        }
    }
    @Override
    public boolean isUserValid(String username, String password) throws UserNotFoundException {
        // Validate input
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new UserNotFoundException("Invalid username or password.");
        }

        // Hash the provided password with the same method used during registration
        // String hashedPassword = hashPassword(password);

        // Validate the user using the DAO
        try {
            return userDAO.isUserValid(username, password);
        } catch (UserDAOException e) {
            throw new UserNotFoundException("Error while validating user.", e);
        }
    }

    private boolean isInputValid(UserDTO userDTO) {
        // Add your validation logic here
        // This is a very basic example: check that the username and password are not null or empty
        return userDTO.getUsername() != null && !userDTO.getUsername().trim().isEmpty()
                && userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty();
    }
}
