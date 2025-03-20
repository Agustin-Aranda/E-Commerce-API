package com.revature.repos;

import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.interfaces.ProductDAO;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    @Override
    public Product create(Product obj) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            // Adding returning * to the end will return the user directly added to the database
            String sql = "INSERT INTO PRODUCT (name, description, price, stock) VALUES " +
                    "(?, ?, ?, ?) RETURNING *;";

            PreparedStatement ps = conn.prepareStatement(sql);

            // Set the values
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getDescription());
            ps.setBigDecimal(3, obj.getPrice());
            ps.setInt(4, obj.getStock());

            // Execute the statement
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product p = new Product();
                // Set the properties of the user
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setStock(rs.getInt("stock"));

                return p;

            }

        } catch (SQLException e) {
            System.out.println("Could not save the product");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getAll() {
        List<Product> allProducts = new ArrayList<>();

        Connection conn = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM PRODUCT";

        try {
            // We need to create Statement Object
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {

                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setStock(rs.getInt("stock"));

                allProducts.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Could not get all Products!");
            e.printStackTrace();
        }
        return allProducts;
    }

    @Override
    public Product getById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM PRODUCT WHERE product_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setStock(rs.getInt("stock"));

                return p;
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve product by Id");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product update(Product obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
