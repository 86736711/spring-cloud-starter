package com.npf.cloud.boot.interceptor;

import com.npf.cloud.framework.interceptor.BaseActionInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Description:解决跨域的问题,这个跨域只在没有gateway的基础上使用，所以先注释掉
 *
 *
 *
 *
 * @Author: 宁培峰
 * @Date: Created in 13:26 2018/7/6 0006
*/
//@Component
//@PathPattern(path={"/**"})
public class CorsHandlerInterceptor extends BaseActionInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception
    {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Access-Token-Shop,Access-Token-User,APP-Version-Code,APP-Version-Name,tcsl-s1-privilege");
        response.setHeader("Access-Control-Expose-Headers", "Content-Type, x-requested-with, X-Custom-Header, Access-Token-Shop,Access-Token-User,APP-Version-Code,APP-Version-Name,tcsl-s1-privilege");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception
    {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
    }

    @Override
    public int getExeSerialNumber()
    {
        return 0;
    }
}