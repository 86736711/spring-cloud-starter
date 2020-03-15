package com.npf.cloud.boot.exception;

import com.npf.cloud.framework.common.model.ResponseResult;
import com.npf.cloud.framework.exception.ApplicationException;
import com.npf.cloud.framework.exception.ApplicationMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Description:全局异常处理器
 * @Package:com.npf.cloud.boot.exception
 * @ClassName:GlobaExceptionHandler
 * @Author: 宁培峰
 * @Date:2018年6月15日
 */
@Slf4j
@RestControllerAdvice
public class GlobaExceptionHandler {

    @Resource
    private MessageSource messageSource;


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseResult> exceptionHandler(HttpServletResponse response, Exception e) {

        //业务的异常处理，研发人员不用关心安全返回的问题
        if (e instanceof ApplicationMessageException) {
            ApplicationMessageException error = (ApplicationMessageException) e;
            if(null != error.getMessageSourceBO() && null != messageSource){
                ResponseResult responseResult = null;
                try {
                    responseResult = ResponseResult.
                            failure(messageSource, error.getMessageSourceBO().getMessCode(), error.getMessageSourceBO().getArgs());
                }catch (NoSuchMessageException ex) {
                    log.error("没有找到对应的国际化语言！国际化的key={}",error.getMessageSourceBO().getMessCode());
                    responseResult = ResponseResult.failure("request fail! NoSuchMessage");
                }
                return ResponseEntity.ok(responseResult);
            }

            return ResponseEntity.ok(ResponseResult.failure("request fail!"));
        }else if (e instanceof ApplicationException){
            ApplicationException error = (ApplicationException) e;
            return ResponseEntity.ok(ResponseResult.failure(error.getMessage()));
        }

        //异常的重新包装
        log.error("系统发生了未知错误", e);
        return ResponseEntity.ok(ResponseResult.systemFailure("system error!"));
    }


}
