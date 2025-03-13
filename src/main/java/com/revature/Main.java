package com.revature;

import com.revature.controllers.UserController;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.UserDAO;
import com.revature.repos.UserDAOImpl;
import com.revature.services.UserService;
import com.revature.util.ConnectionUtil;
import com.revature.util.MenusUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static boolean running = true;
    static User loggedInUser = null;

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAOImpl();
        Scanner scan = new Scanner(System.in);
        UserService userService = new UserService(userDAO);
        UserController userController = new UserController(userService, scan);



        while (running) {
            if (loggedInUser == null) {
                // Menú principal
                MenusUtil.PrincipalMenu();
                String input = MenusUtil.getValidInput(scan, new String[]{"q", "1", "2"});

                switch (input) {
                    case "1": loggedInUser = userController.registerNewUser(); break;
                    case "2": loggedInUser = userController.loginUser(); break;
                    case "q": System.out.println("Quitting application!"); running = false; break;
                }
            } else {
                if (loggedInUser.getRole() == Role.USER) {

                    MenusUtil.UserMenu();

                    String choice = MenusUtil.getValidInput(scan, new String[]{"1","2","3","4", "q"});
                    switch (choice) {
                        case "1": break;
                        case "2": break;
                        case "3": break;
                        case "4": break;
                        case "q": loggedInUser = null; break;
                    }
                } else {
                    MenusUtil.AdminMenu();
                    String choice = MenusUtil.getValidInput(scan, new String[]{"1","2","3","4", "q"});

                    switch (choice) {
                        case "1": break;
                        case "2": break;
                        case "3": break;
                        case "4": break;
                        case "q": loggedInUser = null; break;
                    }
                }
            }
        }
    }
}

