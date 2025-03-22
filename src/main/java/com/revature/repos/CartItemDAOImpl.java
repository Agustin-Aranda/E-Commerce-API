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
    public CartItem create(CartItem obj) { return null; }

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
    public CartItem update(CartItem obj) { return null; }

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

    @Override
    public CartItem addToCart(CartItem obj) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Is enough stock from products ?

            String stockSQL = "SELECT stock FROM product WHERE id = ?";
            PreparedStatement stockPs = conn.prepareStatement(stockSQL);
            stockPs.setInt(1, obj.getProductId());
            ResultSet stockRs = stockPs.executeQuery();

            if (stockRs.next()) {
                int availableStock = stockRs.getInt("stock");

                // Does this product already exists in the CARTITEM ?
                String checkSQL = "SELECT quantity FROM cartitem WHERE user_id = ? AND product_id = ?";
                PreparedStatement checkPs = conn.prepareStatement(checkSQL);
                checkPs.setInt(1, obj.getUserId());
                checkPs.setInt(2, obj.getProductId());
                ResultSet rs = checkPs.executeQuery();

                int newQuantity = obj.getQuantity();
                if (rs.next()) {
                    newQuantity += rs.getInt("quantity"); // If already exists only make addition
                }

                // Check if the quantity is more than the available stock
                if (newQuantity > availableStock) {
                    System.out.println("Not enough stock available.");
                    return null;
                }

                // If already exists just update the quantity
                if (rs.next()) {
                    return updateCartQuantity(obj.getUserId(), obj.getProductId(), newQuantity);
                } else {
                    // If it doesn't exist we make the insert into cartItem
                    String insertSQL = "INSERT INTO CARTITEM (user_id, product_id, quantity) VALUES (?, ?, ?) RETURNING *";
                    PreparedStatement insertPs = conn.prepareStatement(insertSQL);
                    insertPs.setInt(1, obj.getUserId());
                    insertPs.setInt(2, obj.getProductId());
                    insertPs.setInt(3, obj.getQuantity());

                    ResultSet insertRs = insertPs.executeQuery();
                    if (insertRs.next()) {
                        //We generate the object CartItem
                        CartItem c = new CartItem();
                        c.setUserId(insertRs.getInt("user_id"));
                        c.setProductId(insertRs.getInt("product_id"));
                        c.setQuantity(insertRs.getInt("quantity"));
                        return c;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not add to Cart");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CartItem removeFromCart(int userId, int productId) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String deleteSQL = "DELETE FROM CARTITEM WHERE user_id = ? AND product_id = ? RETURNING *";
            PreparedStatement ps = conn.prepareStatement(deleteSQL);
            ps.setInt(1, userId);
            ps.setInt(2, productId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CartItem removedItem = new CartItem();
                removedItem.setUserId(rs.getInt("user_id"));
                removedItem.setProductId(rs.getInt("product_id"));
                removedItem.setQuantity(rs.getInt("quantity"));
                return removedItem;
            }
        } catch (SQLException e) {
            System.out.println("Could not remove from Cart");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CartItem updateCartQuantity(int userId, int productId, int newQuantity) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Verify for stock available
            String stockSQL = "SELECT stock FROM PRODUCT WHERE id = ?";
            PreparedStatement stockPs = conn.prepareStatement(stockSQL);
            stockPs.setInt(1, productId);
            ResultSet stockRs = stockPs.executeQuery();

            if (stockRs.next()) {
                int availableStock = stockRs.getInt("stock");

                // If the amount is more than the available stock
                if (newQuantity > availableStock) {
                    System.out.println("Not enough stock available.");
                    return null;
                }

                if (newQuantity > 0) {
                    String updateSQL = "UPDATE CARTITEM SET quantity = ? WHERE user_id = ? AND product_id = ? RETURNING *";
                    PreparedStatement ps = conn.prepareStatement(updateSQL);
                    ps.setInt(1, newQuantity);
                    ps.setInt(2, userId);
                    ps.setInt(3, productId);

                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        CartItem c = new CartItem();
                        c.setUserId(rs.getInt("user_id"));
                        c.setProductId(rs.getInt("product_id"));
                        c.setQuantity(rs.getInt("quantity"));
                        return c;
                    }
                } else {
                    return removeFromCart(userId, productId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not update cart quantity");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM CARTITEM WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CartItem c = new CartItem();
                c.setUserId(rs.getInt("user_id"));
                c.setProductId(rs.getInt("product_id"));
                c.setQuantity(rs.getInt("quantity"));
                cartItems.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Could not retrieve CartItems for user");
            e.printStackTrace();
        }
        return cartItems;
    }
}


