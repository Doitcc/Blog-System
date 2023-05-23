package com.caoxin.handler.security;

import com.alibaba.fastjson.JSON;
import com.caoxin.domain.ResponseResult;
import com.caoxin.enums.CodeEnum;
import com.caoxin.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, 
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        System.out.println("没有权限操作...");
//        accessDeniedException.printStackTrace();
        ResponseResult result = ResponseResult.errorResult(CodeEnum.NO_OPERATOR_AUTH);
        //响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}