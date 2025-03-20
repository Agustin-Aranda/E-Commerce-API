package com.revature.controllers;

import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.PasswordUtil;

import java.util.Scanner;

public class UserController {
    private UserService userService;

    private Scanner scan;

    public UserController(UserService userService, Scanner scan){
        this.userService = userService;
        this.scan = scan;
    }

    // TODO Register a New User
    public User registerNewUser(){
        // Take in user information for the account
        // First name
        System.out.println("What is your first name?");
        String firstName = scan.nextLine();
        // Last name
        System.out.println("What is your last name?");
        String lastName = scan.nextLine();
        // Username
        System.out.println("Enter a Email: ");
        String email = scan.nextLine();

        // Validate the email fits our security metrics
        // TODO tweak logic as needed
        while (userService.validateEmail(email) || !userService.isUsernameAvailable(email)){
            if (userService.validateEmail(email)){
                System.out.println("Email should be a valid email *YourEmail@gmail.com*! Please enter a new Email: ");
                email = scan.nextLine();
            } else {
                System.out.println("Email is already in use! Please enter a new Email: ");
                email = scan.nextLine();
            }
        }

        // Password
        System.out.println("Enter a password: ");
        String password = scan.nextLine();

        while (!userService.validatePassword(password)){
            System.out.println("Password must contain an Uppercase letter, lowercase letter and must be at least 8 characters");
            System.out.println("Please enter a new password: ");
            password = scan.nextLine();
        }

        String hashPassword = PasswordUtil.hashPassword(password);;

        // At this point the email and passwords should valid and available
        System.out.println("You have successfully registered");
        return userService.registerNewUser(firstName, lastName, email, hashPassword);
    }

    // TODO Login a User
    public User loginUser(){
        // Take in a username
        System.out.println("Please enter your email:");
        String email = scan.nextLine();
        userService.validateEmail(email);

        if (userService.validateEmail(email)) {
            System.out.println("Email should be a valid email *YourEmail@gmail.com*! Please enter a new Email: ");
            email = scan.nextLine();
        }
        // Take in a password
        System.out.println("Please enter a password:");
        String password = scan.nextLine();

        // Attempt to login the user
        User returnUser = userService.loginUser(email, password);
        if (returnUser == null){
            System.out.println("Username or password incorrect!");
            return null;
        }

        System.out.println("Welcome back " + returnUser.getFirstName() +" " + returnUser.getLastName()+ "!");
        return returnUser;
    }
}
