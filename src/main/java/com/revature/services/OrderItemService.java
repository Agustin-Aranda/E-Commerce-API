package com.revature.services;

import com.revature.models.OrderItem;
import com.revature.repos.interfaces.OrderItemDAO;

import java.util.List;

public class OrderItemService {
    private final OrderItemDAO orderItemDAO;

    public OrderItemService(OrderItemDAO orderItemDAO) {
        this.orderItemDAO = orderItemDAO;
    }
    //TODO Get All
    public List<OrderItem> allOrderItems(){return orderItemDAO.getAll();}

    //TODO Get ById
    public OrderItem orderItemById(int id){ return orderItemDAO.getById(id); }

    //TODO Delete ById
    public boolean deleterOrderItemById(int id){ return orderItemDAO.deleteById(id);}

    //TODO Place Order
    public List<OrderItem> registerOrder(int userId){
        return orderItemDAO.placeOrder(userId);
    }
}
