package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.OrderStatus;
import com.revature.repos.interfaces.CartItemDAO;
import com.revature.util.ConnectionUtil;
import com.revature.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAOImpl implements CartItemDAO {
    @Override
    public CartItem create(CartItem obj) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            // Adding returning * to the end will return the user directly added to the database
            String sql = "INSERT INTO CARTITEM (cart_item_id, user_id, product_id, quantity) VALUES " +
                    "(?, ?, ?, ?) RETURNING *;";

            PreparedStatement ps = conn.prepareStatement(sql);

            // Set the values
            ps.setInt(1, obj.getCartItemId());
            ps.setInt(2, obj.getUserId());
            ps.setInt(3, obj.getProductId());
            ps.setInt(4, obj.getQuantity());

            // Execute the statement
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CartItem c = new CartItem();
                c.setCartItemId(rs.getInt("cart_item_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setProductId(rs.getInt("product_id"));
                c.setQuantity(rs.getInt("quantity"));

                return c;
            }

        } catch (SQLException e) {
            System.out.println("Could not save CartItem");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CartItem> getAll() {
        List<CartItem> allCartItems = new ArrayList<>();

        Connection conn = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM CARTITEM";

        try {
            // We need to create Statement Object
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {

                CartItem ca = new CartItem();
                ca.setCartItemId(rs.getInt("cart_item_id"));
                ca.setUserId(rs.getInt("user_id"));
                ca.setProductId(rs.getInt("product_id"));
                ca.setQuantity(rs.getInt("quantity"));

                allCartItems.add(ca);
            }

        } catch (SQLException e) {
            System.out.println("Could not get all CartItems!");
            e.printStackTrace();
        }
        return allCartItems;
    }

    @Override
    public CartItem getById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM CARTITEM WHERE cart_item_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CartItem ca = new CartItem();
                ca.setCartItemId(rs.getInt("cart_item_id"));
                ca.setUserId(rs.getInt("user_id"));
                ca.setProductId(rs.getInt("product_id"));
                ca.setQuantity(rs.getInt("quantity"));

                return ca;
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve CartItem by Id");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CartItem update(CartItem obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("DELETE FROM CARTITEM WHERE cart_item_id = ?");
            ps.setInt(1, id);
            int deletedRows = ps.executeUpdate();

            conn.commit();
            return deletedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting CartItem");
            e.printStackTrace();
        }
        return false;
    }
}
