package com.sophia.springbootmall.service;

import com.sophia.springbootmall.dto.CreateOrderRequest;
import com.sophia.springbootmall.model.Order;

public interface OrderService {

    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

}
