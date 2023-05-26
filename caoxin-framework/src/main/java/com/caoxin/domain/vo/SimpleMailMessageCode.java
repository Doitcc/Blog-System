package com.caoxin.domain.vo;

import lombok.Data;
import org.springframework.mail.SimpleMailMessage;

@Data
public class SimpleMailMessageCode extends SimpleMailMessage {
    private String code;
}
