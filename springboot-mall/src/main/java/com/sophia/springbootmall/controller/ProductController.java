package com.sophia.springbootmall.controller;


import com.sophia.springbootmall.constant.ProductCategory;
import com.sophia.springbootmall.dto.ProductQueryParams;
import com.sophia.springbootmall.dto.ProductRequest;
import com.sophia.springbootmall.model.Product;
import com.sophia.springbootmall.service.ProductService;
import com.sophia.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(
            //以下為查詢條件Filtering參數
            @RequestParam(required = false) ProductCategory category,
            //required = false代表category是可選的參數非必選
            @RequestParam(required = false) String search,
            //required = false代表關鍵字搜尋非必選

            //以下為排序 Sorting參數
            @RequestParam(defaultValue = "created_date") String orderBy,
            //根據created_date作排序
            @RequestParam(defaultValue = "desc") String sort,
            //升幂asc或降幂desc排序

            //分頁 Pagination
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            //取得幾筆數據,最少不能<0，避免前端傳負數過來
            @RequestParam(defaultValue = "0") @Min(0) Integer offset //挑過多少筆數據
            //加了@Max和@Min,最上面的class一定要加@Validated，才會生效

    ){

        //設計productQueryParams，代表以後無論增加多少條件參數都不用再檢查Service,Dao，統一在一個class修改
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        //取得product list
        List<Product> productList = productService.getProducts(productQueryParams);

        //取得product 總數
        Integer total =productService.countProduct(productQueryParams); //根據查詢條件算出商品總筆數有多少


        //分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResult(productList); //商品列表放到page裡面

        return ResponseEntity.status(HttpStatus.OK).body(page);
        //不管有沒有搜尋到商品都回傳200給前端，和單一搜尋不同
    }



    //查詢單ㄧ商品
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
                 Product product = productService.getProductById(productId);

                 //因為有指定網址，所以要做下面檢查是否有該商品
                if(product != null){
                    return ResponseEntity.status(HttpStatus.OK).body(product);
                }else{
                    return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
    }

    //新增商品
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
    //只要有加＠NotNull的註解，驗證從前端傳過來的參數。要記得在參數前面加上＠Valid，容易忘記。
        Integer productId = productService.createProduct(productRequest);
        //預期Service會提供一個createProduct方法，會去資料庫生成的productId傳回來給我們

        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product); //回傳一個Http狀態碼給前端

    }

    //修改商品
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest){
        //使用productRequest參數的原因是因為不允許前端修改ID,創建日期等參數，所以使用ProductRequest非常適合

        // 先檢查product是否存在
        Product product = productService.getProductById(productId);

        if (product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //修改商品數據
        //先預設定Service會回傳updateProduct方法，
        // 第一個參數代表要修改哪個商品，第二個商品是修改後的值是什麼
        productService.updateProduct(productId,productRequest);

        Product updateProduct = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);

    }

    //刪除商品
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){

        productService.deleteProductById(productId);

        //對前端來說，只要這個商品不存在了，就代表刪除商品功能成功 No_content
        //不需要增加404 NOt_Found檢查
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
