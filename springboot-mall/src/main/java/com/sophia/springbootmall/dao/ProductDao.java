package com.sophia.springbootmall.dao;

import com.sophia.springbootmall.dto.ProductRequest;
import com.sophia.springbootmall.model.Product;

public interface ProductDao {


    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);


    public void updateProduct(Integer productId, ProductRequest productRequest);
}
