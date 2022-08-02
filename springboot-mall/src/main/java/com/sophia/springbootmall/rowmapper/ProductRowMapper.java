package com.sophia.springbootmall.rowmapper;

import com.sophia.springbootmall.constant.ProductCategory;
import com.sophia.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));



        String categoryStr = resultSet.getString("category");//取出資料庫的值
        ProductCategory category = ProductCategory.valueOf(categoryStr); //將字串轉換成enum類型
        product.setCategory(category);


        //product.setCategory(ProductCategory.valueOf(resultSet.getString("category")));


        product.setImageUrl(resultSet.getString("image_url"));
        product.setPrice(resultSet.getInt("price"));
        product.setDescription(resultSet.getString("description"));
        product.setCreatedDate(resultSet.getTimestamp("created_date"));
        product.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));

        return product;
    }
}
