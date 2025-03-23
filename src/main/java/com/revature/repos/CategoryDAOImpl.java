package com.revature.repos;

import com.revature.models.Category;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.interfaces.CategoryDAO;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    @Override
    public Category create(Category obj) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "INSERT INTO CATEGORY (category_id =?, name =?) VALUES " +
                    "(?,?) RETURNING *;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,obj.getCategoryId());
            ps.setString(2, obj.getName());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category c = new Category();
                c.setName(rs.getString("name"));
                return c;
            }

        } catch (SQLException e) {
            System.out.println("Could not save the Category");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Category> getAll() {
        List<Category> allCategories = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM CATEGORY";

        try {
            // We need to create Statement Object
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {

                Category ca = new Category();
                ca.setCategoryId(rs.getInt("category_id"));
                ca.setName(rs.getString("name"));

                allCategories.add(ca);
            }

        } catch (SQLException e) {
            System.out.println("Could not get all Categories!");
            e.printStackTrace();
        }
        return allCategories;
    }

    @Override
    public Category getById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM CATEGORY WHERE category_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category ca = new Category();

                ca.setCategoryId(rs.getInt("category_id"));
                ca.setName(rs.getString("name"));
                return ca;
            }
        } catch (SQLException e) {
            System.out.println("Could not retrieve category by Id");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category update(Category obj) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "UPDATE CATEGORY SET  name = ? WHERE category_id = ? RETURNING *;";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getCategoryId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category c  = new Category();
                c.setCategoryId(rs.getInt("category_id"));
                c.setName(rs.getString("name"));
                return c;
            }
        } catch (SQLException e) {
            System.out.println("Could not update the category ");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("DELETE FROM CATEGORY WHERE category_id = ?");
            ps.setInt(1, id);
            int deletedRows = ps.executeUpdate();

            conn.commit();
            return deletedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting Category");
            e.printStackTrace();
        }
        return false;
    }
}
