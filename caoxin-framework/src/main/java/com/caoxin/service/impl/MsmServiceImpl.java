package com.caoxin.service.impl;

import com.caoxin.domain.ResponseResult;
import com.caoxin.enums.CodeEnum;
import com.caoxin.handler.mail.SendMsmAsyc;
import com.caoxin.service.MsmService;
import com.caoxin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MsmServiceImpl implements MsmService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisUtils redisUtils;

    //读取配置文件中username的值并赋值给form
    @Value("${spring.mail.username}")
    private String emailSender;
    
    @Autowired
    private SendMsmAsyc sendMsmAsyc;
    
    @Override
    public ResponseResult sendMail(String emailReceiver) {

        String code = redisUtils.getCacheObject("emailCode:" + emailReceiver);
        if (code!=null){
            return ResponseResult.errorResult(CodeEnum.CODE_HAS_SEND);
        }

        code = UUID.randomUUID().toString().replaceAll("[^0-9a-z]","").substring(0, 6);
        
        
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件发送者
        message.setFrom(emailSender);
        // 设置邮件接收者
        message.setTo(emailReceiver);
        // 设置邮件的主题
        message.setSubject("登录验证码");
        // 设置邮件的正文

        String text = "【博客】您的验证码为：" + code + " ,5分钟内有效,请勿泄露给他人。";
        message.setText(text);
        // 发送邮件
        try {
//            long before = System.currentTimeMillis();
//            javaMailSender.send(message);
//            sendMessage(message);
            sendMsmAsyc.sendMessage(message); //异步方法，因为太慢了，所以单独拎出去
//            long mid=System.currentTimeMillis();
//            System.out.println(mid-before); //18s
            redisUtils.setCacheObject("emailCode:"+emailReceiver,code,5, TimeUnit.MINUTES);
            long after=System.currentTimeMillis();
//            System.out.println(after-mid);//1ms
            return ResponseResult.okResult();
        } catch (MailException e) {
            e.printStackTrace();
        }
        return ResponseResult.errorResult(CodeEnum.CODE_SEND_ERROR);
    }
}
