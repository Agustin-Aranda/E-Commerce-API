package com.revature;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM usuarios;";
        List<User> allUsers = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();

            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {

                User u = new User();
                u.setUserId(res.getInt("user_id"));
                u.setFirstName(res.getString("first_name"));
                u.setLastName(res.getString("last_name"));
                u.setEmail(res.getString("email"));
                u.setPassword(res.getString("password"));
                u.setRole(Role.valueOf(res.getString("role")));

                allUsers.add(u);
            }

            for (User u : allUsers) {
                System.out.println("ID: " + u.getUserId() +
                        ", FirstName: " + u.getFirstName() +
                        ", LastName: " + u.getLastName() +
                        ", Email: " + u.getEmail() +
                        ", Rol: " + u.getRole());
            }

        } catch (SQLException e) {
            System.out.println("Could not get all the users!");
            e.printStackTrace();
        }

    }
}