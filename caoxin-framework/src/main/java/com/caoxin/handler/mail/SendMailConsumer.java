package com.caoxin.handler.mail;

import com.caoxin.domain.vo.SimpleMailMessageCode;
import com.caoxin.utils.RedisUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RabbitListener(queues = "sendEmailQueue")
public class SendMailConsumer {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisUtils redisUtils;

    @RabbitHandler
    public void mailConsume(SimpleMailMessageCode message) {
        System.out.println("==================================================");
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
        redisUtils.setCacheObject("emailCode:" + Objects.requireNonNull(message.getTo())[0], message.getCode(), 5, TimeUnit.MINUTES);
    }
}
