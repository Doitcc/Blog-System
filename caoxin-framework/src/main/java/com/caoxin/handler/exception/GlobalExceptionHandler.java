package com.caoxin.handler.exception;

import com.caoxin.domain.ResponseResult;
import com.caoxin.enums.CodeEnum;
import com.caoxin.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }


    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(CodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResult maxUploadSizeExceptionHandler(MaxUploadSizeExceededException e){
        log.error("上传文件大于2MB",e);
        return ResponseResult.errorResult(CodeEnum.IMG_MAX_ERROR);
    }
}
