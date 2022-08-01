package com.sophia.springbootmall.dao;

import com.sophia.springbootmall.model.Product;

public interface ProductDao {


    Product getProductById(Integer productId);
}
