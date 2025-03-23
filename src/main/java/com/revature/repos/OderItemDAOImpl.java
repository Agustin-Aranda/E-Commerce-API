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
        List<OrderItem> orderItems = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()) {
            conn.setAutoCommit(false); // Begin transaction

            // Get the products from the cart
            String getCartSQL = "SELECT product_id, quantity FROM CARTITEM WHERE user_id = ?";
            List<OrderItem> items = new ArrayList<>();
            try (PreparedStatement cartPs = conn.prepareStatement(getCartSQL)) {
                cartPs.setInt(1, userId);
                try (ResultSet cartRs = cartPs.executeQuery()) {
                    while (cartRs.next()) {
                        items.add(new OrderItem(cartRs.getInt("product_id"), cartRs.getInt("quantity")));
                    }
                }
            }
            if (items.isEmpty()) {
                throw new SQLException("Cart is empty, cannot place order.");
            }

            // Calcular el precio total de la orden
            BigDecimal totalPrice = calculateTotalPrice(items, conn);

            // Insertar la orden y obtener su ID
            String insertOrderSQL = "INSERT INTO orden (user_id, total_price, status, created_at) VALUES (?, ?, ?, ?) RETURNING order_id";
            int orderId;
            try (PreparedStatement orderPs = conn.prepareStatement(insertOrderSQL)) {
                orderPs.setInt(1, userId);
                orderPs.setBigDecimal(2, totalPrice);
                orderPs.setString(3, OrderStatus.PENDING.name());
                orderPs.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

                try (ResultSet orderRs = orderPs.executeQuery()) {
                    if (!orderRs.next()) {
                        throw new SQLException("Error inserting order, no ID returned.");
                    }
                    orderId = orderRs.getInt("order_id");
                }
            }

            // Insertar items de la orden y actualizar stock en un solo paso
            String insertOrderItemSQL = "INSERT INTO orderitem (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            String updateStockSQL = "UPDATE product SET stock = stock - ? WHERE product_id = ? AND stock >= ?";

            for (OrderItem item : items) {
                // Get the price
                String getProductPriceSQL = "SELECT price, stock FROM product WHERE product_id = ?";
                BigDecimal productPrice;
                int stock;
                try (PreparedStatement productPs = conn.prepareStatement(getProductPriceSQL)) {
                    productPs.setInt(1, item.getProductId());
                    try (ResultSet productRs = productPs.executeQuery()) {
                        if (!productRs.next()) {
                            throw new SQLException("Product not found: " + item.getProductId());
                        }
                        productPrice = productRs.getBigDecimal("price");
                        stock = productRs.getInt("stock");
                    }
                }
                // Check the available stock first
                if (item.getQuantity() > stock) {
                    throw new SQLException("Not enough stock for product ID: " + item.getProductId());
                }

                // Insert In order Item
                try (PreparedStatement orderItemPs = conn.prepareStatement(insertOrderItemSQL)) {
                    orderItemPs.setInt(1, orderId);
                    orderItemPs.setInt(2, item.getProductId());
                    orderItemPs.setInt(3, item.getQuantity());
                    orderItemPs.setBigDecimal(4, productPrice);
                    orderItemPs.executeUpdate();
                }

                // Update the stock
                try (PreparedStatement stockPs = conn.prepareStatement(updateStockSQL)) {
                    stockPs.setInt(1, item.getQuantity());
                    stockPs.setInt(2, item.getProductId());
                    stockPs.setInt(3, item.getQuantity());
                    if (stockPs.executeUpdate() == 0) {
                        throw new SQLException("Stock update failed for product ID: " + item.getProductId());
                    }
                }
                // Add to the list
                item.setPrice(productPrice);
                orderItems.add(item);
            }
            // Drop the cart
            try (PreparedStatement clearCartPs = conn.prepareStatement("DELETE FROM cartitem WHERE user_id = ?")) {
                clearCartPs.setInt(1, userId);
                clearCartPs.executeUpdate();
            }
            conn.commit(); // Confirmar transacción

        } catch (SQLException e) {
            System.out.println("Could not place order: " + e.getMessage());
            e.printStackTrace();
        }
        return orderItems;
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
