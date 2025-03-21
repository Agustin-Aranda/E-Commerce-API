package com.revature.models;

public class CartItem {
    private int CartItemId;

    private int UserId;

    private int ProductId;

    private int Quantity;

    public CartItem(int cartItemId, int userId, int productId, int quantity){
        this.CartItemId = cartItemId;
        this.UserId = userId;
        this.ProductId = productId;
        this.Quantity = quantity;
    }

    public CartItem() {

    }

    //Getters and Setters

    public int getCartItemId() {return CartItemId;}

    public void setCartItemId(int cartItemId) {CartItemId = cartItemId;}

    public int getUserId() {return UserId;}

    public void setUserId(int userId) {this.UserId = userId;}

    public int getQuantity() {return Quantity;}

    public void setQuantity(int quantity) {Quantity = quantity;}

    public int getProductId() {return ProductId;}

    public void setProductId(int productId) {this.ProductId = productId;}
}
