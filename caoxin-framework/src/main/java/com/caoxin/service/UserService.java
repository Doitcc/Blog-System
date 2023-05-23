package com.caoxin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.User;


public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user,String code);

    ResponseResult getUserOverview();

    ResponseResult forgetPassword(String userName, String password,String code);
}
