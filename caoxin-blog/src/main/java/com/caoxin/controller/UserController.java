package com.caoxin.controller;

import com.caoxin.logannotation.SystemLog;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.User;
import com.caoxin.service.MsmService;
import com.caoxin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MsmService msmService;

    @GetMapping("/userInfo")
    public ResponseResult userInfo() {
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user, String code) {
        return userService.register(user, code);
    }
    
    @GetMapping("/sendEmail")
    public ResponseResult sendEmail(String emailReceiver) {
        return msmService.sendMail(emailReceiver);
    }
    
    @PostMapping("/forgetPassword")
    public ResponseResult ForgetPassword(String userName,String password,String code){
        return userService.forgetPassword(userName,password,code);
    }

    @GetMapping("/userOverview")
    public ResponseResult userOverview() {
        return userService.getUserOverview();
    }
}