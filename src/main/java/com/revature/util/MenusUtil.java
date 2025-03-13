package com.revature.util;

import java.util.Scanner;

public class MenusUtil {
    public static void PrincipalMenu(){
        System.out.println("====================================");
        System.out.println("          *** BIENVENIDO ***        ");
        System.out.println("====================================");
        System.out.println("1. Sing Up");
        System.out.println("2. Log In");
        System.out.println("q. Exit");
        System.out.println("====================================");
        System.out.print("Choose an option: ");
    }

    public static void UserMenu() {
        System.out.println("====================================");
        System.out.println("            *** USER MENU ***        ");
        System.out.println("====================================");
        System.out.println("1. Account Settings");
        System.out.println("2. See Products");
        System.out.println("3. Make Order");
        System.out.println("4. Order History");
        System.out.println("q. Exit");
        System.out.println("====================================");
        System.out.print("Please select an option: ");
    }

    public static void AdminMenu() {
        System.out.println("====================================");
        System.out.println("           *** ADMIN MENU ***        ");
        System.out.println("====================================");
        System.out.println("1. Manage Users");
        System.out.println("2. Manage Products");
        System.out.println("3. View Orders");
        System.out.println("4. Update Order Status");
        System.out.println("q. Exit");
        System.out.println("====================================");
        System.out.print("Please select an option: ");
    }


    // Validate the input in the menu
    public static String getValidInput(Scanner scan, String[] validOptions) {
        String input;
        while (true) {
            input = scan.nextLine();
            for (String option : validOptions) {
                if (input.equals(option)) {
                    return input;
                }
            }
            System.out.println("Invalid input. Please enter a valid value: ");
        }
    }
}
