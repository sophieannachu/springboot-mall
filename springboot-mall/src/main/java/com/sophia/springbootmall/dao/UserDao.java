package com.sophia.springbootmall.dao;

import com.sophia.springbootmall.dto.UserRegisterRequest;
import com.sophia.springbootmall.model.User;

public interface UserDao {

    User getUserById(Integer userId);

    Integer createUser(UserRegisterRequest userRegisterRequest);

}
