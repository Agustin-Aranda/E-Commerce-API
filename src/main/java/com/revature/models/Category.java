package com.revature.models;

public class Category {
    private int CategoryId;

    private String Name;

    public Category(int categoryId, String name){
        this.CategoryId = categoryId;
        this.Name = name;
    }

    public Category() {

    }

    public String getName() { return Name; }

    public void setName(String name) { Name = name; }

    public int getCategoryId() { return CategoryId; }

    public void setCategoryId(int categoryId) { CategoryId = categoryId; }
}
