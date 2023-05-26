package com.caoxin.handler.mail;

import com.caoxin.domain.vo.SimpleMailMessageCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendMailProducer {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void mailProduce(SimpleMailMessageCode message){
        rabbitTemplate.convertAndSend("sendEmailExchange","email_route_key",message);
    }
}
