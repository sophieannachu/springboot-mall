package com.sophia.springbootmall.service.impl;

import com.sophia.springbootmall.dao.UserDao;
import com.sophia.springbootmall.dto.UserRegisterRequest;
import com.sophia.springbootmall.model.User;
import com.sophia.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
}
