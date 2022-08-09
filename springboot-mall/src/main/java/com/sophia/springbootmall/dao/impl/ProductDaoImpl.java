package com.sophia.springbootmall.dao.impl;

import com.sophia.springbootmall.constant.ProductCategory;
import com.sophia.springbootmall.dao.ProductDao;
import com.sophia.springbootmall.dto.ProductQueryParams;
import com.sophia.springbootmall.dto.ProductRequest;
import com.sophia.springbootmall.model.Product;
import com.sophia.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) FROM product WHERE 1=1";

        Map<String, Object>  map = new HashMap<>();

        //查詢條件
        if(productQueryParams.getCategory() !=null){  //一定要在一開始做是否null的檢查
            sql = sql + " AND category = :category"; // AND前面一定要留空白鍵
            map.put("category", productQueryParams.getCategory().name()); //將enum類型轉成字串再加到map裡面
        }

        if(productQueryParams.getSearch() !=null){
            sql = sql + " AND product_name LIKE :search" ; // AND前面一定要留空白鍵
            map.put("search", "%" + productQueryParams.getSearch() + "%"); // 注意%不能寫在上面SQL語句裡
        }

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        //queryForObject通常用在取得count值
        //Integer.class表示將count轉換成Integer的返回值，再讓total接住

        return total;
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM product WHERE 1=1";
        //WHERE 1=1 在資料庫中沒有影響的寫法，但在Java中就可以拼接到WHERE 1=1後面
        //變成 WHERE 1=1 AND category = :category
        //如果category =null，也不會有影響，多個查詢條件就會很明顯

        Map<String, Object>  map = new HashMap<>();

        //查詢條件
        if(productQueryParams.getCategory() !=null){  //一定要在一開始做是否null的檢查
            sql = sql + " AND category = :category"; // AND前面一定要留空白鍵！
            map.put("category", productQueryParams.getCategory().name()); //將enum類型轉成字串再加到map裡面
        }

        if(productQueryParams.getSearch() !=null){
            sql = sql + " AND product_name LIKE :search" ; // AND前面一定要留空白鍵！
            map.put("search", "%" + productQueryParams.getSearch() + "%"); // 注意%不能寫在上面SQL語句裡
        }

        //排序
        //ORDER BY和 Sort原本就已預設好，所以不用再做null檢查～
        sql = sql +" ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();
        // 注意不能寫成 ORDER by :orderBy

        //分頁
        sql = sql + " LIMIT :limit OFFSET :offset "; //取多少筆數據, 略過多少筆
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        return productList;
    }

    //查詢商品
    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "From product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (productList.size()>0){
            return productList.get(0);
        }else {
            return null;
        }
    }

    //新增商品
    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product(product_name, category, image_url, price, stock, " +
                "description, created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, " +
                ":description, :createdDate, :lastModifiedDate)";

        Map<String,Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date(); //紀錄當下時間點當作最後時間
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder(); //儲存資料庫自動生成的productId

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId =keyHolder.getKey().intValue(); //再將productId回傳出去

        return productId;
    }

    //修改商品
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "Update product SET product_name = :productName, category = :category, image_url = :imageUrl, " +
                "price = :price, stock = :stock, description = :description, last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("lastModifiedDate", new Date()); //修改商品數據時，也要修改最後修改時間的值

        namedParameterJdbcTemplate.update(sql,map);
    }

    //刪除商品
    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
