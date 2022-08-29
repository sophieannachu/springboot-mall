package com.sophia.springbootmall.service.impl;

import com.sophia.springbootmall.dao.UserDao;
import com.sophia.springbootmall.dto.UserLoginRequest;
import com.sophia.springbootmall.dto.UserRegisterRequest;
import com.sophia.springbootmall.model.User;
import com.sophia.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

@Component
public class UserServiceImpl implements UserService {

    //import sl4j的logger
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {

        //檢查email是否被註冊過
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if(user != null){

            //{}內的值會按照順序放參數
            log.warn("該 email {} 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5 生成密碼的雜湊值
        String hashPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashPassword);

        //創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }


    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        //檢查 user是否存在
        if(user == null){
            log.warn("該 email{}尚未註冊" , userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //回傳400給前端
        }

        //使用MD5 生成密碼的雜湊值
        String hashPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());


        //比較密碼
        if(user.getPassword().equals(hashPassword)){
            return user; //user存在在資料庫內的值 equals 前端傳過來的值, 不可使用「==」or「=」
        }else{
            log.warn("email{}的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
