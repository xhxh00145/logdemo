package com.log.logdemo.exceptionhandler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常拦截
    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerException(NotLoginException e) {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }
}

