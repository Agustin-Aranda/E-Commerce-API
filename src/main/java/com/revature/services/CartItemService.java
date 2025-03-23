package com.revature.services;

import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.repos.interfaces.CartItemDAO;
import com.revature.repos.interfaces.ProductDAO;

import java.util.List;

public class CartItemService {
    private ProductDAO productDAO;
    private CartItemDAO cartItemDAO;

    public CartItemService(ProductDAO productDAO, CartItemDAO cartItemDAO) {
        this.productDAO = productDAO;
        this.cartItemDAO = cartItemDAO;
    }

    //TODO Get All
    public List<CartItem> allCartItems(){return cartItemDAO.getAll();}

    //TODO Get ById
    public CartItem cartItemById(int id){ return cartItemDAO.getById(id); }

    //TODO Get ByUserId
    public List<CartItem> cartItemsByUserId(int id){ return cartItemDAO.getCartItemsByUserId(id); }

    //TODO Get CartItem
    public CartItem cartItemProduct(int userId, int productId){
        return cartItemDAO.getCartItemsByUserIdAndProductId(userId,productId);
    }

    //TODO Add to Cart one Item
    public CartItem registerCartItem(int userId, int productId, int quantity) {
        //If the product exist continue
        Product product = productDAO.getById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        //If the quantity is bigger than the stock throw an exception
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        //If the cartItem already exist only update the quantity
        CartItem existingItem = cartItemDAO.getCartItemsByUserIdAndProductId(userId, productId);
        int newQuantity = (existingItem != null) ? existingItem.getQuantity() + quantity : quantity;
        //If the new quantity is more than the stock
        if (newQuantity > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        return (existingItem != null)
                ? updateCartQuantityFromCardItem(userId, productId, newQuantity)
                : cartItemDAO.addToCart(new CartItem(0, userId, productId, quantity));
    }

    //TODO Delete cartItem
    public CartItem DeleteFromCart(int userId, int productId) {
        CartItem existingItem = cartItemDAO.getCartItemsByUserIdAndProductId(userId, productId);
        if (existingItem == null) {
            throw new IllegalArgumentException("Cart item not found.");
        }
        return cartItemDAO.removeFromCart(userId, productId);
    }

    //TODO Update CartItem
    public CartItem updateCartQuantityFromCardItem(int userId, int productId, int newQuantity) {
        Product product = productDAO.getById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        if (newQuantity > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }
        if (newQuantity > 0) { return cartItemDAO.updateCartQuantity(userId, productId, newQuantity); }
        else { return cartItemDAO.removeFromCart(userId, productId);}
    }



}
