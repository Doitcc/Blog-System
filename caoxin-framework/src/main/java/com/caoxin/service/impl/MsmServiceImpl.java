package com.caoxin.service.impl;

import com.caoxin.domain.ResponseResult;
import com.caoxin.enums.CodeEnum;
import com.caoxin.service.MsmService;
import com.caoxin.utils.RedisUtils;
import com.caoxin.utils.SendMailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MsmServiceImpl implements MsmService {
    @Autowired
    private RedisUtils redisUtils;

    //读取配置文件中username的值并赋值给emailSender
    @Value("${spring.mail.username}")
    private String emailSender;
    
    @Autowired
    private SendMailUtils sendMailUtils;
    

    @Override
    public ResponseResult sendMail(String emailReceiver) {

        String code = redisUtils.getCacheObject("emailCode:" + emailReceiver);
        if (code != null) {
            return ResponseResult.errorResult(CodeEnum.CODE_HAS_SEND);
        }
        sendMailUtils.sendMail(emailReceiver,emailSender);
        return ResponseResult.okResult();
    }
}
