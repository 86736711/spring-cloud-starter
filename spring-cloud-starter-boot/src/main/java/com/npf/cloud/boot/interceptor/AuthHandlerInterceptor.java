package com.npf.cloud.boot.interceptor;


import com.npf.cloud.framework.context.RequestContext;
import com.npf.cloud.framework.context.RequestContextHandler;
import com.npf.cloud.framework.interceptor.BaseActionInterceptor;
import com.npf.cloud.framework.interceptor.annotation.ExcludePathPattern;
import com.npf.cloud.framework.interceptor.annotation.PathPattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @package: com.tcsl.slyun.starter.cors
 * @project-name: sly
 * @description: 登录认证拦截器,/api/system/getServerName /api/auth/getName 测试路径
 * @author: Created by
 * @create-datetime: 2017-04-26 14-58
 */
@Slf4j
@Component
@PathPattern(path = "/api/**")
@ExcludePathPattern(path = {
        "/**/v2/api-docs"
})
public class AuthHandlerInterceptor extends BaseActionInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * 这里根据业务来更改代码来实现自己的权限过来的问题
         */



        //通过拦截器设置请求的上下文，提供给controller来使用
        RequestContext requestContext = new RequestContext();
        requestContext.setRequest(request);
        requestContext.setResponse(response);
        RequestContextHandler.setContext(requestContext);

        // 从HTTP请求头中获取Locale参数
        String i18n = request.getHeader("Locale");
        if (StringUtils.isNotBlank(i18n)) {
            // 动态设置语言
            String[] split = i18n.split("_");
            Locale locale = new Locale(split[0], split[1]);
            LocaleContextHolder.setLocale(locale);
        } else {
            // 设置默认为中文
            LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
        }


        //一些不需要拦截的资源
        //spring静态资源处理不拦截
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }


        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContextHandler.clean();
    }


    @Override
    public int getExeSerialNumber() {
        return 1;
    }
}
