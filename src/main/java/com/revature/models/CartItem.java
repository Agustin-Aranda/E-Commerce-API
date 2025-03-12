package com.revature.models;

public class CartItem {
    private int CartItemId;

    private User user;

    private Product product;

    private int Quantity;

    //Getters and Setters

    public int getCartItemId() {return CartItemId;}

    public void setCartItemId(int cartItemId) {CartItemId = cartItemId;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public int getQuantity() {return Quantity;}

    public void setQuantity(int quantity) {Quantity = quantity;}

    public Product getProduct() {return product;}

    public void setProduct(Product product) {this.product = product;}
}
