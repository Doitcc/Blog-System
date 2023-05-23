package com.caoxin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.LoginUser;
import com.caoxin.domain.entity.User;
import com.caoxin.domain.vo.BlogUserLoginVo;
import com.caoxin.domain.vo.UserInfoVo;
import com.caoxin.service.BlogLoginService;
import com.caoxin.service.UserService;
import com.caoxin.utils.BeanCopyUtils;
import com.caoxin.utils.JwtUtil;
import com.caoxin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisUtils redisUtils;
    
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        
//        LoginUser loginUser1 = redisUtils.getCacheObject("bloglogin:" + userId);
//        if(loginUser1!=null){
//            System.out.println("====================================");
//            redisUtils.deleteObject("bloglogin:" + userId);
//        }
        
        String jwt = JwtUtil.createJWT(userId);
        loginUser.setToken(jwt);
        
        //把用户信息存入redis
        redisUtils.setCacheObject("bloglogin:" + userId, loginUser, 1, TimeUnit.HOURS);
//        redisUtils.setCacheObject("bloglogin:" + userName, loginUser, 1, TimeUnit.HOURS);

        //把token和userinfo封装 返回
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);

        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //获取userid
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisUtils.deleteObject("bloglogin:" + userId);
        return ResponseResult.okResult();
    }
    
}
