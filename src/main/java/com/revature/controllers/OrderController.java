package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Order;
import com.revature.models.OrderStatus;
import com.revature.models.Role;
import com.revature.services.OrderService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class OrderController {
    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);


    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    // TODO Get all orders (Admin Only)
    public void getAllOrdersHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }
        List<Order> orders = orderService.getAllOrders();
        ctx.json(orders);
    }

    // TODO Get Order by ID (Admin Only)
    public void getOrderByIdHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }
        int orderId;
        try {
            orderId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid Order ID format!"));
            return;
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            ctx.status(404).json(new ErrorMessage("Order not found!"));
            return;
        }

        ctx.json(order);
    }

    // TODO Get Orders by User ID ( User or Admin)
    public void getOrdersByUserIdHandler(Context ctx) {
        int sessionUserId = ctx.sessionAttribute("UserId");
        int userId;
        try {
            userId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid User ID format!"));
            return;
        }

        if (sessionUserId != userId && !isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("You are not authorized to view these orders!"));
            return;
        }

        List<Order> orders = orderService.getOrderByUserId(userId);
        if (orders.isEmpty()) {
            ctx.status(404).json(new ErrorMessage("No orders found for this user!"));
            return;
        }

        ctx.json(orders);
    }

    // TODO Get Orders by Status (Admin Only)
    public void getOrdersByStatusHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }

        OrderStatus status;
        try {
            status = OrderStatus.valueOf(ctx.pathParam("status").toUpperCase());
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorMessage("Invalid order status!"));
            return;
        }

        List<Order> orders = orderService.getOrderByStatus(status);
        if (orders.isEmpty()) {
            ctx.status(404).json(new ErrorMessage("No orders found with this status!"));
            return;
        }
        ctx.json(orders);
    }


    // TODO Register a new Order
    public void registerOrderHandler(Context ctx) {
        Order requestOrder = ctx.bodyAsClass(Order.class);

        Order newOrder = orderService.registerOrder(
                requestOrder.getOrderId(),
                requestOrder.getUserId(),
                requestOrder.getTotalPrice(),
                requestOrder.getStatus(),
                LocalDateTime.now()
        );

        if (newOrder == null) {
            ctx.status(500).json(new ErrorMessage("Failed to create order!"));
            return;
        }

        logger.info("New order registered with ID: " + newOrder.getOrderId());
        ctx.status(201).json(newOrder);
    }

    // TODO Update Order Status (Admin Only)
    public void updateOrderStatusHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid Order ID format!"));
            return;
        }

        OrderStatus status;
        try {
            status = ctx.bodyAsClass(Order.class).getStatus();
        } catch (Exception e) {
            ctx.status(400).json(new ErrorMessage("Invalid status format!"));
            return;
        }

        Order updatedOrder = orderService.updateStatusOrder(orderId, status);
        if (updatedOrder == null) {
            ctx.status(500).json(new ErrorMessage("Failed to update order status!"));
            return;
        }

        logger.info("Order ID " + orderId + " status updated to " + status);
        ctx.status(200).json(updatedOrder);
    }

    // TODO Delete an Order (Admin Only)
    public void deleteOrderHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid Order ID format!"));
            return;
        }

        boolean deleted = orderService.deletOrder(orderId);
        if (!deleted) {
            ctx.status(500).json(new ErrorMessage("Failed to delete order!"));
            return;
        }

        logger.info("Order ID " + orderId + " has been deleted.");
        ctx.status(200).json(new ErrorMessage("Order successfully deleted!"));
    }

    public boolean isLogged(Context ctx){
        return ctx.sessionAttribute("UserId") != null;
    }

    public boolean isAdmin(Context ctx) {
        Role role = ctx.sessionAttribute("role");
        return isLogged(ctx) && Role.ADMIN.equals(role);
    }

}
