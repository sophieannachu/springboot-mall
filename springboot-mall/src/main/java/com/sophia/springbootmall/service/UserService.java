package com.sophia.springbootmall.service;

import com.sophia.springbootmall.dto.UserRegisterRequest;
import com.sophia.springbootmall.model.User;

public interface UserService {

    User getUserById(Integer userId);
    Integer register(UserRegisterRequest userRegisterRequest);

}
