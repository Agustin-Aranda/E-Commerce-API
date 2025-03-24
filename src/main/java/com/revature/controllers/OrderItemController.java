package com.revature.controllers;
import com.revature.dtos.response.ErrorMessage;
import com.revature.models.OrderItem;
import com.revature.services.OrderItemService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class OrderItemController {

    private final OrderItemService orderItemService;
    private final Logger logger = LoggerFactory.getLogger(OrderItemController.class);

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // TODO Get ALL OrderItems
    public void getAllOrderItemsHandler(Context ctx) {
        List<OrderItem> orderItems = orderItemService.allOrderItems();
        ctx.json(orderItems);
    }

    // TODO Get ById
    public void getOrderItemByIdHandler(Context ctx) {
        int orderItemId;
        try {
            orderItemId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid OrderItem ID format!"));
            return;
        }

        OrderItem orderItem = orderItemService.orderItemById(orderItemId);
        if (orderItem == null) {
            ctx.status(404).json(new ErrorMessage("OrderItem not found!"));
            return;
        }

        ctx.json(orderItem);
    }

    // TODO Delete ById
    public void deleteOrderItemHandler(Context ctx) {
        int orderItemId;
        try {
            orderItemId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid OrderItem ID format!"));
            return;
        }
        if (orderItemService.orderItemById(orderItemId) == null){
            ctx.status(500).json(new ErrorMessage("OrderItem not found!"));
            return;
        }

        boolean deleted = orderItemService.deleterOrderItemById(orderItemId);
        if (!deleted) {
            ctx.status(500).json(new ErrorMessage("Error deleting OrderItem!"));
            return;
        }

        logger.info("OrderItem with ID " + orderItemId + " deleted.");
        ctx.status(200).json(new ErrorMessage("OrderItem successfully deleted!"));
    }

    // TODO Make an Order (User)
    public void placeOrderHandler(Context ctx) {
        int userId;
        try {
            userId = Integer.parseInt(ctx.pathParam("UserId"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid User ID format!"));
            return;
        }

        List<OrderItem> orderItems = orderItemService.registerOrder(userId);
        if (orderItems.isEmpty()) {
            ctx.status(400).json(new ErrorMessage("No items found for this order!"));
            return;
        }

        ctx.status(201).json(orderItems);
    }
}
