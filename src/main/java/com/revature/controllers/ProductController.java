package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.services.ProductService;
import io.javalin.http.Context;

import java.util.List;


public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    // TODO View All products
    public void getAllProductsHandler(Context ctx){
        ctx.json(productService.getAllProducts());
    }

    //TODO View ById
    public void  getProductByIdHandler(Context ctx){

        int ProductId;
        try {
            ProductId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Product Id format!"));
            return;
        }

        Product product = productService.getProductById(ProductId);
        if (product == null) {
            ctx.status(404).json(new ErrorMessage("Product not found!"));
            return;
        }

        ctx.json(product);
    }

    //TODO View ByCategory
    public void  getProductByCategoryHandler(Context ctx){

        int CategoryId;
        try {
            CategoryId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid Category Id format!"));
            return;
        }

        List<Product> products = productService.getProductByCategory(CategoryId);
        if (products == null || products.isEmpty()) {
            ctx.status(404).json(new ErrorMessage("No products found in this category!"));
            return;
        }

        ctx.json(products);
    }

    // TODO create Product
    public void registerProductHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }

        Product requestProduct = ctx.bodyAsClass(Product.class);

        Product newProduct = productService.registerNewProduct(
                    requestProduct.getName(),
                    requestProduct.getDescription(),
                    requestProduct.getPrice(),
                    requestProduct.getStock(),
                    requestProduct.getCategory()
            );
        if ( newProduct == null) {
            ctx.status(400).json(new ErrorMessage("Product could not be saved"));
        } else {
            ctx.status(200).json(new ErrorMessage("Product successfully register!"));
            ctx.json(newProduct);
        }
    }

    //TODO Update product
    public void updateProductHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }

        int ProductId;
        try {
            ProductId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid product ID format!"));
            return;
        }

        Product requestProduct = ctx.bodyAsClass(Product.class);
        try {
            Product updatedProduct = productService.updateProduct(
                    ProductId,
                    requestProduct.getName(),
                    requestProduct.getDescription(),
                    requestProduct.getPrice(),
                    requestProduct.getStock(),
                    requestProduct.getCategory()
            );
            ctx.status(200).json(updatedProduct);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorMessage(e.getMessage()));
        }
    }

    //TODO Delete Product
    public void deleteProductHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }

        int ProductId;
        try {
            ProductId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid product ID format!"));
            return;
        }

        if (productService.getProductById(ProductId) == null){
            ctx.status(400).json(new ErrorMessage("Product Not Found!"));
            return;
        }

        boolean deleted = productService.deleteProduct(ProductId);
        if (!deleted) {
            ctx.status(500).json(new ErrorMessage("Error deleting product!"));
            return;
        }

        ctx.status(200).json(new ErrorMessage("Product successfully deleted!"));
    }

    public boolean isLogged(Context ctx){
        return ctx.sessionAttribute("UserId") != null;
    }

    public boolean isAdmin(Context ctx) {
        Role role = ctx.sessionAttribute("role");
        return isLogged(ctx) && Role.ADMIN.equals(role);
    }
}
