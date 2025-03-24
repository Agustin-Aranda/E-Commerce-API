package com.revature.util;

import com.revature.controllers.*;
import com.revature.repos.*;
import com.revature.repos.interfaces.*;
import com.revature.services.*;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinUtil {
    public static Javalin create(int port){
        // Create all of our variables
        UserDAO userDAO = new UserDAOImpl();
        UserService userService = new UserService(userDAO);
        UserController userController = new UserController(userService);

        ProductDAO productDAO = new ProductDAOImpl();
        ProductService productService = new ProductService(productDAO);
        ProductController productController = new ProductController(productService);

        OrderDAO orderDAO = new OrderDAOImpl();
        OrderService orderService = new OrderService(orderDAO);
        OrderController orderController = new OrderController(orderService);

        OrderItemDAO orderItemDAO = new OderItemDAOImpl();
        OrderItemService orderItemService = new OrderItemService(orderItemDAO);
        OrderItemController orderItemController = new OrderItemController(orderItemService);

        CategoryDAO categoryDAO = new CategoryDAOImpl();
        CategoryService categoryService = new CategoryService(categoryDAO);
        CategoryController categoryController = new CategoryController(categoryService);

        CartItemDAO cartItemDAO = new CartItemDAOImpl();
        CartItemService cartItemService = new CartItemService(productDAO, cartItemDAO);
        CartItemController cartItemController = new CartItemController(cartItemService);



        return Javalin.create(config -> {
                    // Inside of here I have a config variable, this can be used for things like CORS configuration
                    config.router.apiBuilder(() -> {
                        path("/users", () -> {
                            post("/register", userController::registerUserHandler);
                            post("/login", userController::loginHandler);
                            get("/", userController::getAllUsersHandler);
                            get("/email", userController::getUserByEmailHandler);
                            get("/{id}", userController::getUserByIdHandler);
                            put("/{id}", userController::updateUserHandler);
                            delete("/{id}", userController::deleteUserHandler);
                        });
                        path("/products", () -> {
                            get("/", productController::getAllProductsHandler);
                            get("/{id}", productController::getProductByIdHandler);
                            get("/category/{id}", productController::getProductByCategoryHandler);
                            post("/", productController::registerProductHandler);
                            put("/{id}", productController::updateProductHandler);
                            delete("/{id}", productController::deleteProductHandler);
                        });
                        path("/orders", () -> {
                            get("/", orderController::getAllOrdersHandler);
                            get("/{id}", orderController::getOrderByIdHandler);
                            get("/user/{id}", orderController::getOrdersByUserIdHandler);
                            get("/status/{status}", orderController::getOrdersByStatusHandler);
                            post("/", orderController::registerOrderHandler);
                            put("/{id}/status", orderController::updateOrderStatusHandler);
                            delete("/{id}", orderController::deleteOrderHandler);
                        });
                        path("/orderItems", () -> {
                            get("/", orderItemController::getAllOrderItemsHandler);
                            get("/{id}", orderItemController::getOrderItemByIdHandler);
                            delete("/{id}", orderItemController::deleteOrderItemHandler);
                            post("/place/{userId}", orderItemController::placeOrderHandler);
                        });
                        path("/cartItems", () -> {
                            get("/all", cartItemController::getAllCartItemsHandler);
                            get("/{id}", cartItemController::getCartItemByIdHandler);
                            get("/user/{UserId}", cartItemController::getCartItemsByUserIdHandler);
                            get("/user/{UserId}/product/{ProductId}", cartItemController::getCartItemProductHandler);
                            post("/add", cartItemController::addToCartHandler);
                            put("/update", cartItemController::updateCartItemHandler);
                            delete("/delete", cartItemController::deleteFromCartHandler);
                        });
                        path("/categories", () -> {
                            get("/", categoryController::getAllCategoriesHandler);
                            get("/{id}", categoryController::getCategoryByIdHandler);
                            post("/add", categoryController::createCategoryHandler);
                            put("/{id}", categoryController::updateCategoryHandler);
                            delete("/{id}", categoryController::deleteCategoryHandler);
                        });
                    });
                })

                .start(port);
    }
}
