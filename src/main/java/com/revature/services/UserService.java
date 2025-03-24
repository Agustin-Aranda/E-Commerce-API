package com.revature.services;

import com.revature.models.User;
import com.revature.repos.interfaces.UserDAO;
import com.revature.util.PasswordUtil;

import java.util.List;
import java.util.regex.Pattern;

public class UserService {
    private UserDAO userDAO;

    //We use a format for email witch is the way the user is going to aces to the application
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    // TODO Validate Email
    public boolean validateEmail(String email){
        //Email must be in correct email format
            return email != null && pattern.matcher(email).matches();
    }

    // TODO Validate Email availability
    // If a email is already register we want to return false (the email is already using)
    public boolean isEmailAvailable(String email){
        return userDAO.getUserByEmail(email) == null;
    }

    // TODO Validate Password Security
    public boolean validatePassword(String password){
        // Length must be at least 8 and contains an uppercase and lowercase letter
        boolean correctLength = password.length() >= 8;
        boolean hasLowercase = false;
        boolean hasUppercase = false;

        // We're going to break apart the string into a character array and then validate that it contains an uppercase
        // and lower case by using the Wrapper Class methods
        char[] characters = password.toCharArray();
        // Loop through the characters
        for (char c: characters){
            // Validate if the character is uppercase or lowercase
            // Wrapper classes like Integer, Double, Character, etc contain static methods that are useful for working
            // with their primitives and also casting
            if (Character.isUpperCase(c)){
                hasUppercase = true;
            } else if (Character.isLowerCase(c)){
                hasLowercase = true;
            }
        }

        return correctLength && hasLowercase && hasUppercase;
        // If you know regex, this can all be done on one line
    }

    // TODO Register
    public User registerNewUser(String firstName, String lastName, String email, String password){
        // NOTE: We expect our validation methods to be called BEFORE this method is called in the controller layer
        // Create a new user object
        User userToBeSaved = new User(firstName, lastName, email, password);

        // Save the user to the "database"
        return userDAO.create(userToBeSaved);
    }

    // TODO Login
    public User loginUser(String email, String password){
        // Get the user by their email
        User returnedUser = userDAO.getUserByEmail(email);
        if (returnedUser == null){
            return null;
        }
        if(PasswordUtil.checkPassword(password,returnedUser.getPassword())){
            return returnedUser;
        } return null;
    }

    // TODO Get All Users
    public List<User> getAllUsers(){
        return userDAO.getAll();
    }

    // TODO Get  user by Email
    public User getUserByEmail(String email){
        return userDAO.getUserByEmail(email);
    }

    // TODO Get User by Id
    public User getUserById(int id){ return userDAO.getById(id); }

    // TODO Update User
    public User UpdateUser(String firstName, String lastName, String email, String password, int userId) {
        // Get the user by Id
        User existingUser = userDAO.getById(userId);

        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setEmail(email);
        existingUser.setPassword(password);

        return userDAO.update(existingUser);
    }

    //TODO Delete User
    public boolean DeleteUser(int id){ return userDAO.deleteById(id); }

}
