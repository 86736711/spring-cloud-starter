package com.npf.cloud.log;


import com.npf.cloud.framework.common.model.ResponseResult;
import com.npf.cloud.framework.constant.HeaderConstant;
import com.npf.cloud.framework.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @ProjectName: logAspectAutoConfiguration
 * @Package: com.npf.cloud.starter.log
 * @ClassName: logAspectAutoConfiguration
 * @Author: ningpf
 * @Description: 统一的日志框架，打印controller和service的请求返回的信息
 * @Date: 20190624
 * @Version: 1.2.1-SNAPSHOT
 */
@Slf4j
@Aspect
public class logAspectAutoConfiguration {

    @Autowired
    private MessageSource messageSource;



    @Pointcut(value = "execution(* com.npf.cloud..controller.*Controller.*(..))")
    public void controllerPointCut() {
        //Controller层日志拦截及异常统一处理
    }

    @Pointcut(value = "execution(* com.npf.cloud..service..*Service.*(..))||" +
            "execution(* com.npf.cloud..dao.*Dao.*(..))")
    public void servicePointCut() {
        //Service层日志拦截及异常统一处理
    }

    private void strAppend(StringBuilder headerStr,HttpServletRequest request,String headerKey){
        headerStr.append(headerKey+"="+request.getHeader(headerKey)+"|");
    }


    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }


    @Around(value = "controllerPointCut()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        StringBuilder headerStr = new StringBuilder();

        StringBuilder argStr = new StringBuilder();

        //获取关心的header
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //这里能实际记录header的信息
        strAppend(headerStr,request, HeaderConstant.HEADER_USER_TOKEN);


        //获取请求参数
        for (Object object : proceedingJoinPoint.getArgs()) {
            if (null == object) {
                object = "null";
            }
            argStr.append("，param:[" + JSONUtil.toStr(object) + "]");
        }

        //获取请求返回结果
        Object  result = null;
        long start = System.currentTimeMillis();
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("系统异常", throwable);
            result = ResponseEntity.ok().body(ResponseResult.failure());
            throw throwable;
        } finally {
            //真个请求的日志
            long end = System.currentTimeMillis();
            log.info("requestIP:{},请求地址:{}, 请求头:{}, 请求参数:{}, 返回结果:{}, 执行时间:{}",getIp(request), request.getRequestURI(),
                    headerStr.toString(), argStr.toString(),JSONUtil.toStr(result),end - start);
        }
        return result;
    }

    @Around(value = "servicePointCut()")
    public Object doServiceAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        String objMethodName = methodSignature.getMethod().getName();
        Class clazz = proceedingJoinPoint.getSignature().getDeclaringType();
        StringBuffer sb = new StringBuffer();
        sb.append(clazz).append(","+objMethodName);
        for (Object object : proceedingJoinPoint.getArgs()) {
            if (null == object) {
                object = "null";
            }
            sb.append("，param:[" + object + "]");
        }
        Object result = null;
        long start = System.currentTimeMillis();
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            long end = System.currentTimeMillis();
            log.info("请求方法:{}, 返回结果:{}, 执行时间:{}",sb.toString(), JSONUtil.toStr(result),end - start);
        }
        return result;
    }



}
