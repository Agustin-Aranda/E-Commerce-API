package com.revature;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.interfaces.UserDAO;
import com.revature.repos.UserDAOImpl;
import com.revature.services.UserService;
import com.revature.util.JavalinUtil;

import java.util.Scanner;

public class Main {

    static boolean running = true;
    static User loggedInUser = null;

    public static void main(String[] args) {
        JavalinUtil.create(7070);
    }
}

