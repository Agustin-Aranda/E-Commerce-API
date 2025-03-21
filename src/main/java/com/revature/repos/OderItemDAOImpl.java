package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.OrderItem;
import com.revature.models.OrderStatus;
import com.revature.repos.interfaces.OrderItemDAO;
import com.revature.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
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
        List<OrderItem> allOrderItems = new ArrayList<>();

        Connection conn = ConnectionUtil.getConnection();

        String sql = "SELECT * FROM ORDERITEM";

        try {
            // We need to create Statement Object
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {

                OrderItem ordI = new OrderItem();
                ordI.setOrderItemId(rs.getInt("order_item_id"));
                ordI.setOrderId(rs.getInt("order_id"));
                ordI.setProductId(rs.getInt("product_id"));
                ordI.setQuantity(rs.getInt("quantity"));
                ordI.setPrice(rs.getDouble("price"));

                allOrderItems.add(ordI);
            }

        } catch (SQLException e) {
            System.out.println("Could not get all OrdersItems!");
            e.printStackTrace();
        }
        return allOrderItems;
    }

    @Override
    public OrderItem getById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM ORDERITEM WHERE order_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                OrderItem ordI = new OrderItem();
                ordI.setOrderItemId(rs.getInt("order_item_id"));
                ordI.setOrderId(rs.getInt("order_id"));
                ordI.setProductId(rs.getInt("product_id"));
                ordI.setQuantity(rs.getInt("quantity"));
                ordI.setPrice(rs.getDouble("price"));

                return ordI;
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve OrderItem by Id");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OrderItem update(OrderItem obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("DELETE FROM ORDERITEM WHERE order_id = ?");
            ps.setInt(1, id);
            int deletedRows = ps.executeUpdate();

            conn.commit();
            return deletedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting ORDERItem");
            e.printStackTrace();
        }
        return false;
    }
}
