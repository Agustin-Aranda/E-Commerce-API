package com.revature.repos.interfaces;

import com.revature.models.CartItem;

import java.util.List;

public interface CartItemDAO extends GeneralDAO<CartItem> {
    CartItem addToCart(CartItem obj);
    CartItem removeFromCart(int userId, int productId);
    CartItem updateCartQuantity(int userId, int productId, int newQuantity);
    List<CartItem> getCartItemsByUserId(int userId);
    CartItem getCartItemsByUserIdAndProductId(int userId, int productId);
}
