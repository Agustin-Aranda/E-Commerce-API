package com.revature.services;

import com.revature.models.Order;
import com.revature.models.OrderStatus;
import com.revature.repos.interfaces.OrderDAO;

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


}
