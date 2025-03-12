package com.revature.models;

import java.time.LocalDateTime;

public class Order {
    private int OrderId;

    private User user;

    private double TotalPrice;

    private OrderStatus Status;

    private LocalDateTime CreatedAt;

    //Getters and Setters

    public int getOrderId() {return OrderId;}

    public void setOrderId(int orderId) {OrderId = orderId;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public double getTotalPrice() {return TotalPrice;}

    public void setTotalPrice(double totalPrice) {TotalPrice = totalPrice;}

    public OrderStatus getStatus() {return Status;}

    public void setStatus(OrderStatus status) {Status = status;}

    public LocalDateTime getCreatedAt() {return CreatedAt;}

    public void setCreatedAt(LocalDateTime createdAt) {CreatedAt = createdAt;}
}
