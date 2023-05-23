package com.caoxin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.caoxin.constants.SystemConstants;
import com.caoxin.domain.entity.LoginUser;
import com.caoxin.domain.entity.User;
import com.caoxin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查到用户  如果没查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        // TODO 查询权限信息封装
        if(SystemConstants.ADMIN_ID.equals(user.getType())){
            //定义角色权限
            LoginUser loginUser = new LoginUser();
            loginUser.setUser(user);
            loginUser.setPermissions(new ArrayList<>(Arrays.asList("admin")));
//            System.out.println(loginUser.getAuthorities());
            return loginUser;
        }
        return new LoginUser(user,new ArrayList<>(Arrays.asList("ordinary")));
    }
}