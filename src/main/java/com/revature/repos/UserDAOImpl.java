package com.revature.repos;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.interfaces.UserDAO;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {


    @Override
    public User getUserByEmail(String email) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM USUARIOS WHERE EMAIL = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, email);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // This will only return one user so we'll do IF rs.next()
            if (rs.next()) {
                User u = new User();

                u.setUserId(rs.getInt("user_id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setPassword(rs.getString("password"));
                u.setEmail(rs.getString("email"));
                u.setRole(Role.valueOf(rs.getString("role"))); // We need to cast this to an ENUM

                return u;
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve user by email");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User create(User obj) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            // Adding returning * to the end will return the user directly added to the database
            String sql = "INSERT INTO USUARIOS (first_name, last_name, email, password) VALUES " +
                    "(?, ?, ?, ?) RETURNING *;";

            PreparedStatement ps = conn.prepareStatement(sql);

            // Set the values
            ps.setString(1, obj.getFirstName());
            ps.setString(2, obj.getLastName());
            ps.setString(3, obj.getEmail());
            ps.setString(4, obj.getPassword());

            // Execute the statement
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                // Set the properties of the user
                // I can get info from the row in the table by doing
                // rs.getDatatype("column_name") or rs.getDatatype(1) <- column index

                u.setUserId(rs.getInt("user_id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(Role.valueOf(rs.getString("role"))); // We need to cast this to an ENUM

                return u;
            }

        } catch (SQLException e) {
            System.out.println("Could not save user");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> allUsers = new ArrayList<>();

        Connection conn = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM USERS";

        try {
            // We need to create Statement Object
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {

                User u = new User();

                u.setUserId(rs.getInt("user_id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(Role.valueOf(rs.getString("role")));

                allUsers.add(u);
            }


        } catch (SQLException e) {
            System.out.println("Could not get all users!");
            e.printStackTrace();
        }

        return allUsers;
    }

    @Override
    public User getById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM USUARIOS WHERE user_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // This will only return one user so we'll do IF rs.next()
            if (rs.next()) {
                User u = new User();

                u.setUserId(rs.getInt("user_id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setPassword(rs.getString("password"));
                u.setEmail(rs.getString("email"));
                u.setRole(Role.valueOf(rs.getString("role")));

                return u;
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve user by Id");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User obj) {
        return null;
    }


    @Override
    public boolean deleteById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("DELETE FROM USUARIOS WHERE user_id = ?");
            ps.setInt(1, id);
            int deletedRows = ps.executeUpdate();

            conn.commit();
            return deletedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting user");
            e.printStackTrace();
        }
        return false;
    }
}
