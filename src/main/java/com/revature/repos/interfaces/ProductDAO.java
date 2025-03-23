package com.revature.repos.interfaces;

import com.revature.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO extends GeneralDAO<Product>{
     List<Product> getByCategory(int categoryId);
}
