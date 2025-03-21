package com.revature.repos.interfaces;

import com.revature.models.Product;

import java.util.List;

public interface ProductDAO extends GeneralDAO<Product>{
     List<Product> getByCategory(int categoryId);
}
