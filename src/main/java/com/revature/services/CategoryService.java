package com.revature.services;

import com.revature.models.Category;
import com.revature.repos.interfaces.CategoryDAO;

import java.util.List;
import java.util.concurrent.CancellationException;

public class CategoryService {
    private CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO){
        this.categoryDAO = categoryDAO;
    }

    //TODO Get All
    public List<Category> allCategories(){ return categoryDAO.getAll(); }

    //TODO Get ById
    public Category categoryById(int id){ return categoryDAO.getById(id); }

    //TODO Update category
    public Category updateCategory(String name, int id){
        Category existingCategory = categoryDAO.getById(id);
        if (existingCategory == null){
            throw new IllegalArgumentException("The category does not exist");
        }
        existingCategory.setName(name);
        return categoryDAO.update(existingCategory);
    }

    //TODO Create category
    public Category registerCategory(int categoryId, String name){
        Category categoryToBeSaved = new Category(categoryId, name);
        return categoryDAO.create(categoryToBeSaved);
    }

    //TODO Delete category
    public boolean deleteCategory(int id){ return categoryDAO.deleteById(id); }
}
