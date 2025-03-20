package com.revature.repos;

import com.revature.models.OrderItem;
import com.revature.repos.interfaces.OrderItemDAO;

import java.util.List;

public class OderItemDAOImpl implements OrderItemDAO {
    @Override
    public OrderItem create(OrderItem obj) {
        return null;
    }

    @Override
    public List<OrderItem> getAll() {
        return List.of();
    }

    @Override
    public OrderItem getById(int id) {
        return null;
    }

    @Override
    public OrderItem update(OrderItem obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
