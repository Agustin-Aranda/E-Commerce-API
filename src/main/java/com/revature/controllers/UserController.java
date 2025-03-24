package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.PasswordUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    public UserController(UserService userService){
        this.userService = userService;
    }
    //TODO Register new user
    public void registerUserHandler(Context ctx){
        User requestUser = ctx.bodyAsClass(User.class);

        if (!userService.validateEmail(requestUser.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email should be a valid email *YourEmail@gmail.com*!"));
            return;
        }

        if (!userService.isEmailAvailable(requestUser.getEmail())) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Email is already in use!"));

            // Log a warning for a user attempting to register with a taken name
            logger.warn("register attemp for taking email" + requestUser.getEmail());
            return;
        }

        if (!userService.validatePassword(requestUser.getPassword())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Password must contain an Uppercase letter, lowercase letter and must be at least 8 characters"));
            return;
        }

        String hashPassword = PasswordUtil.hashPassword(requestUser.getPassword());

        User registeredUser = userService.registerNewUser(
                requestUser.getFirstName(),
                requestUser.getLastName(),
                requestUser.getEmail(),
                hashPassword);
        //If something fails we'll report a server error
        if (registeredUser == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }

        logger.info("New user registered with email: " + registeredUser.getEmail());

        ctx.status(202);
        ctx.json(registeredUser);
    }

    //TODO Login a user
    public void loginHandler(Context ctx){
        // Get the user from the body
        User requestUser = ctx.bodyAsClass(User.class);
        // Attempt to login
        User returnedUser = userService.loginUser(requestUser.getEmail(), requestUser.getPassword());

        // If invalid let them know email or password incorrect
        if (returnedUser == null){
            ctx.json(new ErrorMessage("Email or Password Incorrect"));
            ctx.status(400);
            return;
        }
        // If valid return the user and add them to the session
        ctx.status(200);
        ctx.json(returnedUser);

        // Add the userId to the session
        ctx.sessionAttribute("UserId", returnedUser.getUserId());
        ctx.sessionAttribute("role", returnedUser.getRole());
        System.out.println("Role stored in session: " + returnedUser.getRole());

    }

    // TODO (Admin Only) View All Users
    public void getAllUsersHandler(Context ctx){
        if (!isAdmin(ctx)) {
            ctx.status(403).result("Access denied");
            return;
        }
        // If admin show the users
        ctx.json(userService.getAllUsers());
    }

    //TODO Get UserById (Admin Only)
    public void getUserByIdHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).result("Access denied");
            return;
        }
        int userId;
        try {
            userId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid user ID format!"));
            return;
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            ctx.status(404);
            ctx.json(new ErrorMessage("User not found!"));
            return;
        }

        ctx.json(user);
    }


    //TODO Get UserByEmail (Admin Only)
    public void getUserByEmailHandler(Context ctx){
        if (!isAdmin(ctx)) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Access denied"));
            return;
        }

        User requestUser = ctx.bodyAsClass(User.class);

        if(!userService.validateEmail(requestUser.getEmail())){
            ctx.status(400);
            ctx.json(new ErrorMessage("Email should be a valid email *ExampleEmail@gmail.com*!"));
            return;
        }
        if (userService.getUserByEmail(requestUser.getEmail()) == null){
            ctx.status(404);
            ctx.json(new ErrorMessage("USER NOT FOUND!"));
            return;
        }
        ctx.json(userService.getUserByEmail(requestUser.getEmail()));
    }

    //TODO Update User (User quality)
    public void updateUserHandler(Context ctx) {
        if (!isLogged(ctx)) {
            ctx.status(403);
            ctx.json(new ErrorMessage("You must log in first!"));
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid user ID format!"));
            return;
        }

        int sessionUserId = ctx.sessionAttribute("UserId");
        if (sessionUserId != userId) {
            ctx.status(403);
            ctx.json(new ErrorMessage("You are not authorized to update this user!"));
            return;
        }

        User requestUser = ctx.bodyAsClass(User.class);
        User existingUser = userService.getUserById(userId);

        if (existingUser == null) {
            ctx.status(404);
            ctx.json(new ErrorMessage("User not found!"));
            return;
        }

        // Check the email
        if (!existingUser.getEmail().equals(requestUser.getEmail())) {
            if (!userService.validateEmail(requestUser.getEmail()) || !userService.isEmailAvailable(requestUser.getEmail())) {
                ctx.status(400);
                ctx.json(new ErrorMessage("Invalid or already used email!"));
                return;
            }
        }

        // Check and hash the password for security
        String updatedPassword = existingUser.getPassword();
        if (requestUser.getPassword() != null && !requestUser.getPassword().isEmpty()) {
            if (!userService.validatePassword(requestUser.getPassword())) {
                ctx.status(400);
                ctx.json(new ErrorMessage("Password must contain an uppercase letter, lowercase letter, and at least 8 characters!"));
                return;
            }
            updatedPassword = PasswordUtil.hashPassword(requestUser.getPassword());
        }

        User updatedUser = userService.UpdateUser(
                requestUser.getFirstName(),
                requestUser.getLastName(),
                requestUser.getEmail(),
                updatedPassword,
                userId
        );

        if (updatedUser == null) {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong while updating the user!"));
            return;
        }

        logger.info("User " + updatedUser.getUserId() + " updated their information.");
        ctx.status(200);
        ctx.json(updatedUser);
    }


    // TODO Delete an User (Admin Only)
    public void deleteUserHandler(Context ctx) {
            if (!isAdmin(ctx)) {
                ctx.status(403);
                ctx.json(new ErrorMessage("You must be an admin to perform this action!"));
                return;
            }

            int userId;
            try {
                userId = Integer.parseInt(ctx.pathParam("id"));
            } catch (NumberFormatException e) {
                ctx.status(400);
                ctx.json(new ErrorMessage("Invalid user ID format!"));
                return;
            }

            User userToDelete = userService.getUserById(userId);
            if (userToDelete == null) {
                ctx.status(404);
                ctx.json(new ErrorMessage("User not found!"));
                return;
            }

            boolean deleted = userService.DeleteUser(userId);
            if (!deleted) {
                ctx.status(500);
                ctx.json(new ErrorMessage("Error deleting user!"));
                return;
            }

            logger.info("User with email " + userToDelete.getEmail() + " has been deleted.");
            ctx.status(200);
            ctx.json(new ErrorMessage("User successfully deleted!"));
    }

    public boolean isLogged(Context ctx){
        return ctx.sessionAttribute("UserId") != null;
    }

    public boolean isAdmin(Context ctx) {
        Role role = ctx.sessionAttribute("role");
        return isLogged(ctx) && Role.ADMIN.equals(role);
    }

}
