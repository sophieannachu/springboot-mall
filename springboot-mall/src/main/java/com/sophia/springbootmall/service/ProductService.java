package com.sophia.springbootmall.service;

import com.sophia.springbootmall.dto.ProductRequest;
import com.sophia.springbootmall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    public void updateProduct(Integer productId, ProductRequest productRequest);

    public void deleteProductById(Integer productId);

}
