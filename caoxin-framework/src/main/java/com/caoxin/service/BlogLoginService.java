package com.caoxin.service;

import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
