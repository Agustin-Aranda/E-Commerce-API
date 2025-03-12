package com.revature.models;

import java.math.BigDecimal;

public class Product {
    private int ProductId;

    private String Name;

    private String Description;

    private BigDecimal Price;

    private int Stock;

    // Getters and Setters

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
