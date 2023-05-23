package com.caoxin.handler.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class SendMsmAsyc {
    @Autowired
    private JavaMailSender javaMailSender;

    //异步请求
    @Async
    public void sendMessage(SimpleMailMessage message) {
        javaMailSender.send(message);
    }
}
