package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
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
                requestUser.getPassword());
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



}
