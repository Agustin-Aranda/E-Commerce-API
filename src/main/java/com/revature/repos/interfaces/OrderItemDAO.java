package com.revature.repos.interfaces;

import com.revature.models.OrderItem;

import java.util.List;

public interface OrderItemDAO extends GeneralDAO<OrderItem> {
    List<OrderItem> placeOrder(int userId);
}
