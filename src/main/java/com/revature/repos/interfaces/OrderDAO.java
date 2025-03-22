package com.revature.repos.interfaces;

import com.revature.models.Order;
import com.revature.models.OrderStatus;

import java.util.List;

public interface OrderDAO extends GeneralDAO<Order> {
    Order updateStatus(Order obj, OrderStatus status);
    List<Order> getByUserId(int userId);
    List<Order> getByStatus(OrderStatus status);
}
