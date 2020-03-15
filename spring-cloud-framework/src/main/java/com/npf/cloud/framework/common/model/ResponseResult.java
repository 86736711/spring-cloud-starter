package com.npf.cloud.framework.common.model;


import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.util.Locale;

/**
 * @ProjectName: tcsl-smart-framework
 * @Package: cn.com.tcsl.s1.common.model
 * @ClassName: ResponseResult
 * @Author: ningpf
 * @Description: API统一返回结果
 * @Date: 20190624
 * @Version: 1.0
 */
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    //成功的返回码
    public static final String SUCCESS_CODE = "1";

    //业务异常的返回码
    public static final String FAIL_CODE = "0";

    //系统异常的返回码
    public static final String SYSTEM_ERROR_CODE = "SYSTEM_ERROR";


    /*返回码*/
    private String code;

    /*返回数据对象*/
    private T data;

    /*返回信息*/
    private String message;



    public ResponseResult() {
    }


    /**
     * @Method getMessage
     * @Author ningpf
     * @Version 1.0
     * @param messageSource，国际化源对象
     * @param code，返回消息的code码
     * @param args，这个参数可以是多个，是给国际化中占位符的填充
     * @Description 返回国际化消息
     * @Return String 对应code的国际化返回值
     * @Date 20190624
     */
    public static String getMessage(MessageSource messageSource, String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code,args,locale);
    }

    /**
     * @Method failure
     * @Author ningpf
     * @Version 1.0
     * @param messageSource，国际化源对象
     * @param code，返回消息的code码
     * @param args，这个参数可以是多个，是给国际化中占位符的填充
     * @Description 返回国际化消息
     * @Return String 对应code的国际化返回值
     * @Date 20190624
     */
    public static ResponseResult failure(MessageSource messageSource, String code, Object... args) {
        ResponseResult responseResult = new ResponseResult();
        Locale locale = LocaleContextHolder.getLocale();
        responseResult.setCode(FAIL_CODE);
        responseResult.setMessage(messageSource.getMessage(code, args, locale));
        return responseResult;
    }



    /**
     * @Method failure
     * @Author ningpf
     * @Version 1.0
     * @param mes，返回的消息
     * @Description 返回国际化消息
     * @Return ResponseResult 对应api的结果体
     * @Date 20190624
     */
    public static ResponseResult failure(String mes) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(FAIL_CODE);
        responseResult.setMessage(mes);
        return responseResult;
    }


    /**
     * @Method failure
     * @Author ningpf
     * @Version 1.0
     * @param mes，返回的消息
     * @Description 返回系统异常
     * @Return ResponseResult 对应api的结果体
     * @Date 20190624
     */
    public static ResponseResult systemFailure(String mes) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(SYSTEM_ERROR_CODE);
        responseResult.setMessage(mes);
        return responseResult;
    }


    /**
     * @Method failure
     * @Author ningpf
     * @Version 1.0
     * @Description 返回国际化消息
     * @Return ResponseResult 对应api的结果体
     * @Date 20190624
     */
    public static ResponseResult failure() {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(FAIL_CODE);
        return responseResult;
    }




    /**
     * @Method success
     * @Author ningpf
     * @Version 1.0
     * @Description 返回成功的结构体
     * @Return ResponseResult 返回消息的api
     * @Date 20190624
     */
    public static ResponseResult success() {
        return success(null);
    }

    /**
     * @Method success
     * @Author ningpf
     * @Version 1.0
     * @param data，api接口返回的数据
     * @Description 返回成功的结构体
     * @Return ResponseResult 返回消息的api
     * @Date 20190624
     */
    public static ResponseResult success(Object data) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(SUCCESS_CODE);
        responseResult.setData(data);
        return responseResult;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
