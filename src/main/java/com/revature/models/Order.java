package com.revature.models;

import java.time.LocalDateTime;

public class Order {
    private int OrderId;

    private int UserId;

    private double TotalPrice;

    private OrderStatus Status;

    private LocalDateTime CreatedAt;

    public Order(int orderId, int userId, double totalPrice, OrderStatus status, LocalDateTime createdAt){
        this.OrderId = orderId;
        this.UserId = userId;
        this.TotalPrice = totalPrice;
        // Set default value of status
        this.Status = status.PENDING;
        this.CreatedAt = createdAt;
    }

    public Order() {

    }

    //Getters and Setters

    public int getOrderId() {return OrderId;}

    public void setOrderId(int orderId) {OrderId = orderId;}

    public int getUserId() {return UserId;}

    public void setUserId(int userId) {this.UserId = userId;}

    public double getTotalPrice() {return TotalPrice;}

    public void setTotalPrice(double totalPrice) {TotalPrice = totalPrice;}

    public OrderStatus getStatus() {return Status;}

    public void setStatus(OrderStatus status) {Status = status;}

    public LocalDateTime getCreatedAt() {return CreatedAt;}

    public void setCreatedAt(LocalDateTime createdAt) {CreatedAt = createdAt;}
}
