package com.revature.models;

public class OrderItem {
    private int OrderItemId;

    private Order order;

    private Product product;

    private int Quantity;

    private double Price;

    // Getters and Setters
    public int getOrderItemId() { return OrderItemId;}

    public void setOrderItemId(int orderItemId) { OrderItemId = orderItemId;}

    public Product getProduct() { return product;}

    public void setProduct(Product product) { this.product = product;}

    public Order getOrder() { return order; }

    public void setOrder(Order order) { this.order = order;}

    public int getQuantity() { return Quantity;}

    public void setQuantity(int quantity) { Quantity = quantity;}

    public double getPrice() { return Price;}

    public void setPrice(double price) { Price = price;}
}
