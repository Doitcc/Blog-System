package com.caoxin.utils;

import com.caoxin.domain.vo.SimpleMailMessageCode;
import com.caoxin.handler.mail.SendMailProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SendMailUtils {
    @Autowired
    private SendMailProducer sendMailProducer;
    @Autowired
    private RedisUtils redisUtils;

    public void sendMail(String emailReceiver, String emailSender) {

        String code = UUID.randomUUID().toString().replaceAll("[^0-9a-z]", "").substring(0, 6);
//        SimpleMailMessage message = new SimpleMailMessage();
        SimpleMailMessageCode message=new SimpleMailMessageCode();
        // 设置邮件发送者
        message.setFrom(emailSender);
        // 设置邮件接收者
        message.setTo(emailReceiver);
        // 设置邮件的主题
        message.setSubject("登录验证码");
        // 设置邮件的正文
        String text = "【博客】您的验证码为：" + code + " ,5分钟内有效,请勿泄露给他人。";
        message.setText(text);
        
        message.setCode(code);
        // 发送邮件
        sendMailProducer.mailProduce(message);
    }

}
