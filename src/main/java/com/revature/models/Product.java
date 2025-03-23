package com.revature.models;

import java.math.BigDecimal;

public class Product {
    private int ProductId;

    private String Name;

    private String Description;

    private BigDecimal Price;

    private int Stock;

    private int CategoryId;

    public Product (String name, String description, BigDecimal price, int stock, int categoryId){
        this.Name = name;
        this.Description = description;
        this.Price = price;
        this.Stock = stock;
        this.CategoryId = categoryId;
    }
    public Product (int productId, String name, String description, BigDecimal price, int stock, int categoryId){
        this.ProductId = productId;
        this.Name = name;
        this.Description = description;
        this.Price = price;
        this.Stock = stock;
        this.CategoryId = categoryId;

    }

    public Product() {

    }

    // Getters and Setters

    public int getCategory() { return CategoryId; }

    public void setCategory(int category) { CategoryId = category; }

    public int getProductId() {return ProductId;}

    public void setProductId(int productId) {ProductId = productId;}

    public String getDescription() {return Description;}

    public void setDescription(String description) {Description = description;}

    public String getName() {return Name;}

    public void setName(String name) {Name = name;}

    public BigDecimal getPrice() {return Price;}

    public void setPrice(BigDecimal price) {Price = price;}

    public int getStock() {return Stock;}

    public void setStock(int stock) {Stock = stock;}
}
