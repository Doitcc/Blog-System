package com.caoxin.service;

import com.caoxin.domain.ResponseResult;

public interface MsmService {
    ResponseResult sendMail(String emailReceiver);
    
}
