package com.caoxin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitConfig {

    /**
     * 配置交换机、队列、路由键相关
     *
     * @return
     */
    @Bean
    public Queue emailQueue() {
        return new Queue("sendEmailQueue");
    }

    @Bean
    public DirectExchange emailDirectExchange() {
        return new DirectExchange("sendEmailExchange");
    }

    @Bean
    Binding bindEmailQueue() {
        return BindingBuilder.bind(emailQueue()).to(emailDirectExchange()).with("email_route_key");
    }
}