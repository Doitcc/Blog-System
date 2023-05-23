package com.caoxin.filter;

import com.alibaba.fastjson.JSON;
import com.caoxin.domain.ResponseResult;
import com.caoxin.domain.entity.LoginUser;
import com.caoxin.enums.CodeEnum;
import com.caoxin.utils.JwtUtil;
import com.caoxin.utils.RedisUtils;
import com.caoxin.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //说明该接口不需要登录  直接放行
            filterChain.doFilter(request, response);
            return;
        }
        
        //解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时  token非法
//            System.out.println("登录后操作...");
            //响应告诉前端需要重新登录
            ResponseResult result = ResponseResult.errorResult(CodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId = claims.getSubject();
//        System.out.println(userId);
        System.out.println();
        //从redis中获取用户信息
        LoginUser loginUser = redisUtils.getCacheObject("bloglogin:" + userId);
        //如果获取不到
        if (Objects.isNull(loginUser)) {
//            System.out.println("登录过期，请重新登录...");
            //说明登录过期  提示重新登录
            ResponseResult result = ResponseResult.errorResult(CodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }else if (!token.equals(loginUser.getToken())) {
//            System.out.println("账号在别处登录，请重新登录...");
            //说明账号在别处登录，重新登录
            ResponseResult result = ResponseResult.errorResult(CodeEnum.ACCOUNT_LOGGED_IN_ELSEWHERE);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }


}