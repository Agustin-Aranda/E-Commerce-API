package com.revature.repos;

import com.revature.models.Order;
import com.revature.repos.interfaces.OrderDAO;

import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order create(Order obj) {
        return null;
    }

    @Override
    public List<Order> getAll() {
        return List.of();
    }

    @Override
    public Order getById(int id) {
        return null;
    }

    @Override
    public Order update(Order obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
