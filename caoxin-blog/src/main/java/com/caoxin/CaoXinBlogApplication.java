package com.caoxin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.caoxin.mapper")
@EnableScheduling
public class CaoXinBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaoXinBlogApplication.class, args);
    }
}
