package com.revature.controllers;

import com.revature.services.ProductService;


public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }
}
