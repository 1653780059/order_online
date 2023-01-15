package com.example.order_online.handler;

import com.example.order_online.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 16537
 * @Classname GlobalExceptionHandler
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/15 14:16
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result throwableHandler(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}
