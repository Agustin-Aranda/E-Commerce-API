package com.revature.repos;

import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.OrderStatus;
import com.revature.repos.interfaces.OrderDAO;
import com.revature.util.ConnectionUtil;
import com.revature.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order create(Order obj) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            // Adding returning * to the end will return the user directly added to the database
            String sql = "INSERT INTO ORDEN (order_id, user_id, total_price, status, created_at) VALUES " +
                    "(?, ?, ?, ?, ?) RETURNING *;";

            PreparedStatement ps = conn.prepareStatement(sql);

            // Set the values
            ps.setInt(1, obj.getOrderId());
            ps.setInt(2, obj.getUserId());
            ps.setDouble(3, obj.getTotalPrice());
            ps.setString(4, obj.getStatus().toString());
            ps.setDate(5, DateUtil.getSqlDate());


            // Execute the statement
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Order o = new Order();

                o.setOrderId(rs.getInt("order_id"));
                o.setUserId(rs.getInt("user_id"));
                o.setTotalPrice(rs.getDouble("total_price"));
                o.setStatus( OrderStatus.valueOf(rs.getString("status")));
                o.setCreatedAt( rs.getTimestamp("created_at").toLocalDateTime());

                return o;
            }

        } catch (SQLException e) {
            System.out.println("Could not save the Order");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> getAll() {
        List<Order> allOrders = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM ORDEN";

        try {
            // We need to create Statement Object
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {

                Order or = new Order();
                or.setOrderId(rs.getInt("order_id"));
                or.setUserId(rs.getInt("user_id"));
                or.setTotalPrice(rs.getDouble("total_price"));
                or.setStatus(OrderStatus.valueOf(rs.getString("status")));
                or.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                allOrders.add(or);
            }
        } catch (SQLException e) {
            System.out.println("Could not get all Orders!");
            e.printStackTrace();
        }
        return allOrders;
    }

    @Override
    public Order getById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM ORDEN WHERE order_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            // Execute the query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Order or = new Order();

                or.setOrderId(rs.getInt("order_id"));
                or.setUserId(rs.getInt("user_id"));
                or.setTotalPrice(rs.getDouble("total_price"));
                or.setStatus(OrderStatus.valueOf(rs.getString("status")));
                or.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
               return or;
            }
        } catch (SQLException e) {
            System.out.println("Could not retrieve Orden by Id");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Order update(Order obj) {
        return null;
    }

    @Override
    public Order updateStatus(Order obj, OrderStatus status) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "UPDATE ORDEN SET  status = ? WHERE order_id = ? RETURNING *;";

            PreparedStatement ps = conn.prepareStatement(sql);

            // Set the status parameter correctly
            ps.setString(1, status.name()); // Use the 'name()' method of the enum to get the string value
            ps.setInt(2, obj.getOrderId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Order o  = new Order();

                o.setOrderId(rs.getInt("order_id"));
                o.setStatus(OrderStatus.valueOf(rs.getString("status")));

                return o;
            }
        } catch (SQLException e) {
            System.out.println("Could not update the Status ");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("DELETE FROM ORDEN WHERE order_id = ?");
            ps.setInt(1, id);
            int deletedRows = ps.executeUpdate();
            conn.commit();
            return deletedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting Order");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Order> getByUserId(int userId) {
        List<Order> OrdersByUser = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()){

            String sql = "SELECT * FROM ORDEN WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            // We need to create Statement Object
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Order or = new Order();
                or.setOrderId(rs.getInt("order_id"));
                or.setUserId(rs.getInt("user_id"));
                or.setTotalPrice(rs.getDouble("total_price"));
                or.setStatus(OrderStatus.valueOf(rs.getString("status")));
                or.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                OrdersByUser.add(or);
            }
        } catch (SQLException e) {
            System.out.println("Could not get all Orders by UserID!");
            e.printStackTrace();
        }
        return OrdersByUser;
    }

    @Override
    public List<Order> getByStatus(OrderStatus status) {
        List<Order> orders = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM orden WHERE status = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status.name()); // Convert ENUM to String
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setUserId(rs.getInt("user_id"));
                o.setTotalPrice(rs.getDouble("total_price"));
                o.setStatus(OrderStatus.valueOf(rs.getString("status"))); // Convert String to ENUM
                o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                orders.add(o);
            }
        } catch (SQLException e) {
            System.out.println("Could not retrieve orders by status");
            e.printStackTrace();
        }
        return orders;
    }
}
