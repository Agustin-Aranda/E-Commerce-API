package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.OrderItem;
import com.revature.models.OrderStatus;
import com.revature.repos.interfaces.OrderItemDAO;
import com.revature.util.ConnectionUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
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
            ps.setBigDecimal(5, obj.getPrice());

            // Execute the statement
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrderItem or = new OrderItem();
                or.setOrderItemId(rs.getInt("order_item_id"));
                or.setOrderId(rs.getInt("order_id"));
                or.setProductId(rs.getInt("product_id"));
                or.setQuantity(rs.getInt("quantity"));
                or.setPrice(rs.getBigDecimal("price"));

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
                ordI.setPrice(rs.getBigDecimal("price"));

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
                ordI.setPrice(rs.getBigDecimal("price"));

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

    @Override
    public List<OrderItem> placeOrder(int userId) {
        Connection conn = null;
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            conn = ConnectionUtil.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // Get the product of the cart by User
            String getCartSQL = "SELECT product_id, quantity FROM cartitem WHERE user_id = ?";
            PreparedStatement cartPs = conn.prepareStatement(getCartSQL);
            cartPs.setInt(1, userId);
            ResultSet cartRs = cartPs.executeQuery();

            List<OrderItem> items = new ArrayList<>();
            while (cartRs.next()) {
                OrderItem item = new OrderItem();
                item.setProductId(cartRs.getInt("product_id"));
                item.setQuantity(cartRs.getInt("quantity"));
                items.add(item);
            }

            if (items.isEmpty()) {
                throw new SQLException("Cart is empty, cannot place order.");
            }

            // Calculate the total amount
            BigDecimal totalPrice = calculateTotalPrice(items, conn);

            // Insert the order in the table ORDEN
            String insertOrderSQL = "INSERT INTO ORDEN (user_id, total_price, status, created_at) VALUES (?, ?, ?, ?) RETURNING order_id";
            PreparedStatement orderPs = conn.prepareStatement(insertOrderSQL);
            orderPs.setInt(1, userId);
            orderPs.setBigDecimal(2, totalPrice);
            orderPs.setString(3, OrderStatus.PENDING.name());
            orderPs.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            ResultSet orderRs = orderPs.executeQuery();
            int orderId = -1;
            if (orderRs.next()) {
                orderId = orderRs.getInt("order_id");
            }

            // Insert the products on the OrderItems and subtract the quantity in products
            String insertOrderItemSQL = "INSERT INTO ORDERITEM (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            String updateStockSQL = "UPDATE product SET stock = stock - ? WHERE product_id = ? AND stock >= ?";

            for (OrderItem item : items) {
                // Get the price per product
                String getProductPriceSQL = "SELECT price FROM product WHERE product_id = ?";
                PreparedStatement productPs = conn.prepareStatement(getProductPriceSQL);
                productPs.setInt(1, item.getProductId());
                ResultSet productRs = productPs.executeQuery();

                if (productRs.next()) {
                    item.setPrice(productRs.getBigDecimal("price"));
                }

                // insert in OrderItem
                PreparedStatement orderItemPs = conn.prepareStatement(insertOrderItemSQL);
                orderItemPs.setInt(1, orderId);
                orderItemPs.setInt(2, item.getProductId());
                orderItemPs.setInt(3, item.getQuantity());
                orderItemPs.setBigDecimal(4, item.getPrice());
                orderItemPs.executeUpdate();

                // Update the stock in the product table
                PreparedStatement stockPs = conn.prepareStatement(updateStockSQL);
                stockPs.setInt(1, item.getQuantity());
                stockPs.setInt(2, item.getProductId());
                stockPs.setInt(3, item.getQuantity()); // Make sure the stock is enough
                int rowsUpdated = stockPs.executeUpdate();

                if (rowsUpdated == 0) {
                    throw new SQLException("Not enough stock for product ID: " + item.getProductId());
                }

                orderItems.add(item); // Add item to the returned list
            }

            // Drop the user cart
            String clearCartSQL = "DELETE FROM CARTITEM WHERE user_id = ?";
            PreparedStatement clearCartPs = conn.prepareStatement(clearCartSQL);
            clearCartPs.setInt(1, userId);
            clearCartPs.executeUpdate();

            conn.commit(); // Commit the transaction

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Doing a rollback if it goes wrong
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            System.out.println("Could not place order");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Back the state of commit
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
        return orderItems; // Return the list of order items
    }


    private BigDecimal calculateTotalPrice(List<OrderItem> items, Connection conn) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;

        String getPriceSQL = "SELECT price FROM product WHERE product_id = ?";
        PreparedStatement ps = conn.prepareStatement(getPriceSQL);

        for (OrderItem item : items) {
            ps.setInt(1, item.getProductId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                BigDecimal price = rs.getBigDecimal("price");
                total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        return total;
    }
}
