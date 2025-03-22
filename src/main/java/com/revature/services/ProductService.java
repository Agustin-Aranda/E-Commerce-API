package com.revature.services;

import com.revature.models.Product;
import com.revature.models.User;
import com.revature.repos.interfaces.ProductDAO;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO){
        this.productDAO = productDAO;
    }

    //TODO get all Products
    public List<Product> getAllProducts(){return  productDAO.getAll();}

    //TODO get byId
    public Product getProductById(int id){ return productDAO.getById(id); }

    //TODO get by category
    public List<Product> getProductByCategory(int categoryId){
        return productDAO.getByCategory(categoryId);
    }

    //TODO register new product
    public Product registerNewProduct(String name, String description, BigDecimal price, int stock, int categoryId){
        // NOTE: We expect our validation methods to be called BEFORE this method is called in the controller layer
        if (stock <= 0){
            throw new IllegalArgumentException("The stock can't be 0");
        }
        Product productToBeSaved = new Product(name, description, price, stock, categoryId);
        // Save the product to the "database"
        return productDAO.create(productToBeSaved);
    }

    //TODO update Product
    public Product updateProduct(int productId, String name, String description, BigDecimal price, int stock, int categoryId){
        //We expect to make the validations methods here, at least before calling
        Product existingProduct = getProductById(productId);

        if (existingProduct == null){
            throw new IllegalArgumentException("Product not found");
        }

        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setPrice(price);
        existingProduct.setStock(stock);
        existingProduct.setCategory(categoryId);

        return productDAO.update(existingProduct);
    }

    //TODO delete Product
    public boolean deleteProduct(int id){ return productDAO.deleteById(id); }

}
