package com.sophia.springbootmall.controller;


import com.sophia.springbootmall.dto.ProductRequest;
import com.sophia.springbootmall.model.Product;
import com.sophia.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
                 Product product = productService.getProductById(productId);

                if(product != null){
                    return ResponseEntity.status(HttpStatus.OK).body(product);
                }else{
                    return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
    //只要有加＠NotNull的註解，驗證從前端傳過來的參數。要記得在參數前面加上＠Valid，容易忘記。
        Integer productId = productService.createProduct(productRequest); //預期Service會提供一個createProduct方法，會去資料庫生成的productId傳回來給我們

        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product); //回傳一個Http狀態碼給前端

    }
}
