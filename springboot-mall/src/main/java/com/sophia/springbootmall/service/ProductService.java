package com.sophia.springbootmall.service;

import com.sophia.springbootmall.dto.ProductQueryParams;
import com.sophia.springbootmall.dto.ProductRequest;
import com.sophia.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

}
