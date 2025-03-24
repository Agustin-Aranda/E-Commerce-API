package com.revature.controllers;


import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Category;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.CategoryService;
import com.revature.util.PasswordUtil;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryController {
    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);


    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    //TODO Get All Categories
    public void getAllCategoriesHandler(Context ctx){
        ctx.json(categoryService.allCategories());
    }

    //TODO Get ById
    public void getCategoryByIdHandler(Context ctx) {

        int CategoryId;
        try {
            CategoryId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid category ID format!"));
            return;
        }
        Category category = categoryService.categoryById(CategoryId);
        if (category == null) {
            ctx.status(404);
            ctx.json(new ErrorMessage("Category not found!"));
            return;
        }
        ctx.json(category);
    }

    //TODO Create a Category (Admin Only)
    public void createCategoryHandler(Context ctx){
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }
        Category categoryRequest = ctx.bodyAsClass(Category.class);

        Category registerCategory = categoryService.registerCategory(
                categoryRequest.getCategoryId(),
                categoryRequest.getName() );

        //If something fails we'll report a server error
        if (registerCategory == null){
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong!"));
            return;
        }
        logger.info("New category registered with Id: " + registerCategory.getCategoryId());
        ctx.status(202);
        ctx.json(registerCategory);
    }

    //TODO Update Category (Admin Only)
    public void updateCategoryHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }

        int CategoryId;
        try {
            CategoryId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorMessage("Invalid category ID format!"));
            return;
        }

        Category category = ctx.bodyAsClass(Category.class);
        Category existingCategory = categoryService.categoryById(CategoryId);

        if (existingCategory == null) {
            ctx.status(404);
            ctx.json(new ErrorMessage("Category not found!"));
            return;
        }


        Category updatedCategory = categoryService.updateCategory(
                category.getName(),
                CategoryId
        );

        if (updatedCategory == null) {
            ctx.status(500);
            ctx.json(new ErrorMessage("Something went wrong while updating the user!"));
            return;
        }

        logger.info("Category " + updatedCategory.getCategoryId() + " updated their name.");
        ctx.status(200);
        ctx.json(updatedCategory);
    }


    // TODO Delete an category (Admin Only)
    public void deleteCategoryHandler(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.status(403).json(new ErrorMessage("Access denied"));
            return;
        }

        int CategoryId;
        try {
            CategoryId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new ErrorMessage("Invalid Order ID format!"));
            return;
        }

        boolean deleted = categoryService.deleteCategory(CategoryId);
        if (!deleted) {
            ctx.status(500).json(new ErrorMessage("Failed to delete category!"));
            return;
        }

        logger.info("Category " + CategoryId + " has been deleted.");
        ctx.status(200).json(new ErrorMessage("Category successfully deleted!"));
    }


    public boolean isLogged(Context ctx){
        return ctx.sessionAttribute("UserId") != null;
    }

    public boolean isAdmin(Context ctx) {
        Role role = ctx.sessionAttribute("role");
        return isLogged(ctx) && Role.ADMIN.equals(role);
    }


}
