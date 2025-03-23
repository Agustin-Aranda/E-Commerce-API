package com.revature.services;

import com.revature.models.Order;
import com.revature.models.OrderStatus;
import com.revature.repos.interfaces.OrderDAO;
import com.revature.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO){
        this.orderDAO = orderDAO;
    }

    //TODO Get by Status
    public List<Order> getOrderByStatus(OrderStatus status){
        return orderDAO.getByStatus(status);
    }

    //TODO Get byId
    public Order getOrderById(int id){
        return orderDAO.getById(id);
    }

    //TODO Get byUserId
    public List<Order> getOrderByUserId(int id){
        return orderDAO.getByUserId(id);
    }

    //TODO Get all
    public List<Order> getAllOrders(){return orderDAO.getAll();}

    //TODO update status
    public Order updateStatusOrder(int id, OrderStatus status){
        Order existingOrder = orderDAO.getById(id);
        if (existingOrder == null){
            throw new IllegalArgumentException("The order doesn't exist");
        }
        Order updatedOrder = orderDAO.updateStatus(existingOrder, status);
        if (updatedOrder == null) {
            throw new RuntimeException("Failed to update order status.");
        }
        return updatedOrder;
    }

    //TODO Create Order
    public Order registerOrder(int orderId, int userId, double totalPrice, OrderStatus Status, LocalDateTime createdAt){
        Order orderToBeSaved = new Order(orderId , userId, totalPrice, Status, createdAt);
        return orderDAO.create(orderToBeSaved);
    }

    //TODO Delete Order
    public boolean deletOrder(int id){ return orderDAO.deleteById(id); }
}
