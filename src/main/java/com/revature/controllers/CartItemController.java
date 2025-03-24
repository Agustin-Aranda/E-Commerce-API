package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.CartItem;
import com.revature.services.CartItemService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CartItemController {

    private final CartItemService cartItemService;
    private final Logger logger = LoggerFactory.getLogger(CartItemController.class);

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // TODO Get All CartItems (Admin Only)
    public void getAllCartItemsHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }
        List<CartItem> cartItems = cartItemService.allCartItems();
        ctx.json(cartItems);
    }

    // TODO Get CartItem ById (Admin Only)
    public void getCartItemByIdHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }
        int cartItemId;
        try {
            cartItemId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid CartItem ID format!"));
            return;
        }

        CartItem cartItem = cartItemService.cartItemById(cartItemId);
        if (cartItem == null) {
            ctx.status(404).json(new ErrorMessage("CartItem not found!"));
            return;
        }

        ctx.json(cartItem);
    }

    // TODO Get CartItem by UserId
    public void getCartItemsByUserIdHandler(Context ctx) {
        int userId;
        try {
            userId = Integer.parseInt(ctx.pathParam("UserId"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid User ID format!"));
            return;
        }

        List<CartItem> cartItems = cartItemService.cartItemsByUserId(userId);
        if (cartItems.isEmpty()) {
            ctx.status(404).json(new ErrorMessage("No CartItems found for this user!"));
            return;
        }

        ctx.json(cartItems);
    }

    // TODO Get CartItem By User and Product Id
    public void getCartItemProductHandler(Context ctx) {
        int userId, productId;
        try {
            userId = Integer.parseInt(ctx.pathParam("UserId"));
            productId = Integer.parseInt(ctx.pathParam("ProductId"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid User ID or Product ID format!"));
            return;
        }

        CartItem cartItem = cartItemService.cartItemProduct(userId, productId);
        if (cartItem == null) {
            ctx.status(404).json(new ErrorMessage("CartItem not found!"));
            return;
        }

        ctx.json(cartItem);
    }

    // TODO Add to the cart
    public void addToCartHandler(Context ctx) {

        CartItem cartItem = ctx.bodyAsClass(CartItem.class);

        int userId = cartItem.getUserId();
        int productId = cartItem.getProductId();
        int quantity = cartItem.getQuantity();

        try {
             cartItem = cartItemService.registerCartItem(userId, productId, quantity);
            ctx.status(200).json(new ErrorMessage("CartItem successfully Add!"));
            ctx.status(201).json(cartItem);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorMessage("CartItem could be Add!"));
        }
    }

    //TODO Update CartItem
    public void updateCartItemHandler(Context ctx) {

        CartItem cartItem = ctx.bodyAsClass(CartItem.class);
        int userId = cartItem.getUserId();
        int productId = cartItem.getProductId();
        int quantity = cartItem.getQuantity();

        try {
             cartItem = cartItemService.updateCartQuantityFromCardItem(userId, productId, quantity);
            ctx.status(200).json(new ErrorMessage("CartItem successfully updated!"));
            ctx.status(201).json(cartItem);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorMessage("CartItem couldn't be update!"));
        }
    }


    //TODO Delete CartItem
    public void deleteFromCartHandler(Context ctx) {

        CartItem cartItem = ctx.bodyAsClass(CartItem.class);
        int userId = cartItem.getUserId();
        int productId = cartItem.getProductId();

        try {
            cartItem = cartItemService.DeleteFromCart(userId, productId);
            logger.info("Deleted CartItem with ID: " + cartItem.getCartItemId());
            ctx.status(200).json(new ErrorMessage("Order successfully deleted!"));

        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorMessage("Failed to delete order!"));
        }
    }

    public boolean isLogged(Context ctx){
        return ctx.sessionAttribute("UserId") != null;
    }

    public boolean isAdmin(Context ctx){
        return isLogged(ctx) && "ADMIN".equals(ctx.sessionAttribute("role"));
    }

}
