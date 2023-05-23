package com.caoxin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.LeaveMessage;


public interface LeaveMessageService extends IService<LeaveMessage> {
    
    ResponseResult loggedIn(String content);

    ResponseResult notLoggedIn(String content,String avatar);

    ResponseResult getMessage();
    
}
