package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.repos.interfaces.CartItemDAO;

import java.util.List;

public class CartItemDAOImpl implements CartItemDAO {
    @Override
    public CartItem create(CartItem obj) {
        return null;
    }

    @Override
    public List<CartItem> getAll() {
        return List.of();
    }

    @Override
    public CartItem getById(int id) {
        return null;
    }

    @Override
    public CartItem update(CartItem obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
