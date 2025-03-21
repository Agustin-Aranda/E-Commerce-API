package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.OrderItem;
import com.revature.repos.interfaces.OrderItemDAO;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OderItemDAOImpl implements OrderItemDAO {
    @Override
    public OrderItem create(OrderItem obj) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            // Adding returning * to the end will return the user directly added to the database
            String sql = "INSERT INTO ORDERITEM (order_item_id, order_id, product_id, quantity, price) VALUES " +
                    "(?, ?, ?, ?, ?) RETURNING *;";

            PreparedStatement ps = conn.prepareStatement(sql);

            // Set the values
            ps.setInt(1, obj.getOrderItemId());
            ps.setInt(2, obj.getOrderId());
            ps.setInt(3, obj.getProductId());
            ps.setInt(4, obj.getQuantity());
            ps.setDouble(5, obj.getPrice());

            // Execute the statement
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrderItem or = new OrderItem();
                or.setOrderItemId(rs.getInt("order_item_id"));
                or.setOrderId(rs.getInt("order_id"));
                or.setProductId(rs.getInt("product_id"));
                or.setQuantity(rs.getInt("quantity"));
                or.setPrice(rs.getDouble("price"));

                return or;
            }

        }  catch (SQLException e) {
            System.out.println("Could not save CartItem");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OrderItem> getAll() {
        return List.of();
    }

    @Override
    public OrderItem getById(int id) {
        return null;
    }

    @Override
    public OrderItem update(OrderItem obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
