package com.caoxin.controller;

import com.caoxin.domain.ResponseResult;
import com.caoxin.service.LeaveMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class LeaveMessageController {
    @Autowired
    private LeaveMessageService leaveMessageService;

    @PostMapping("/loggedIn")
    public ResponseResult loggedIn(String content) {

        return leaveMessageService.loggedIn(content);
    }

    @PostMapping("/notLoggedIn")
    public ResponseResult notLoggedIn(String content, String avatar) {

        return leaveMessageService.notLoggedIn(content, avatar);
    }
    
    @GetMapping("/getMessage")
    public ResponseResult getMessage(){
        return leaveMessageService.getMessage();
    }
}
