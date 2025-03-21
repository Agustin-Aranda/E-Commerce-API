package com.revature.models;

public class OrderItem {
    private int OrderItemId;

    private int OrderId;

    private int ProductId;

    private int Quantity;

    private double Price;

    public  OrderItem(int orderItemId, int orderId, int productId, int quantity, double price){
        this.OrderItemId = orderItemId;
        this.OrderId = orderId;
        this.ProductId = productId;
        this.Quantity = quantity;
        this.Price = price;
    }
    public OrderItem(){

    }

    // Getters and Setters
    public int getOrderItemId() { return OrderItemId;}

    public void setOrderItemId(int orderItemId) { OrderItemId = orderItemId;}

    public int getProductId() { return ProductId;}

    public void setProductId(int productId) { this.ProductId = productId;}

    public int getOrderId() { return OrderId; }

    public void setOrderId(int order) { this.OrderId = order;}

    public int getQuantity() { return Quantity;}

    public void setQuantity(int quantity) { Quantity = quantity;}

    public double getPrice() { return Price;}

    public void setPrice(double price) { Price = price;}
}
