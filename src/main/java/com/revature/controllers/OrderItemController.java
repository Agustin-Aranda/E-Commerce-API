package com.revature.controllers;

import com.revature.services.OrderItemService;
import com.revature.services.OrderService;

public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService){
        this.orderItemService = orderItemService;
    }
}
