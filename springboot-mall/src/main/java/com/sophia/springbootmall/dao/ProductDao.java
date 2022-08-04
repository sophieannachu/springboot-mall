package com.sophia.springbootmall.dao;

import com.sophia.springbootmall.dto.ProductRequest;
import com.sophia.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {


    List<Product> getProducts();

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);



    public void updateProduct(Integer productId, ProductRequest productRequest);

    public void deleteProductById(Integer productId);
}
