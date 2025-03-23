package com.revature.controllers;


import com.revature.services.CategoryService;

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

}
