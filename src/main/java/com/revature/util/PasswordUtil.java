package com.revature.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    //We make a class for hash the password and save it in the DB
    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String password, String storedHash) {
        return BCrypt.checkpw(password, storedHash);
    }
}
