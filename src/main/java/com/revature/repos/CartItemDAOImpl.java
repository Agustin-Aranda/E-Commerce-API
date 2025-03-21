package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.OrderStatus;
import com.revature.repos.interfaces.CartItemDAO;
import com.revature.util.ConnectionUtil;
import com.revature.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return List.of();
    }

    @Override
    public CartItem getById(int id) {
        return null;
    }

    @Override
    public CartItem update(CartItem obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
