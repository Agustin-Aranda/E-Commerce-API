package com.revature.controllers;

import com.revature.services.CartItemService;

public class CartItemController {
    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService){
        this.cartItemService = cartItemService;
    }

}
