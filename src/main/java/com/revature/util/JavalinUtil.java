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
                            post("/register", userController:: registerUserHandler);
                            //post("/login", userController:: loginHandler);
                            //get("/", userController::getAllUsersHandler);
                        });
                        path("/products", () -> {
                        });
                        path("/orders", () -> {
                        });
                        path("/orderItems", () -> {
                        });
                        path("/cartItems", () -> {
                        });
                        path("/categories", () -> {
                        });
                    });
                })

                .start(port);
    }
}
