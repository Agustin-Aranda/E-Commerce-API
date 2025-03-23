package com.revature.controllers;

import com.revature.services.OrderService;

public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

}
