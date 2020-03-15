package com.npf.cloud.boot.configuration;

import com.npf.cloud.framework.context.SpringContextUtil;
import com.npf.cloud.framework.interceptor.BaseActionInterceptor;
import com.npf.cloud.framework.interceptor.annotation.ExcludePathPattern;
import com.npf.cloud.framework.interceptor.annotation.PathPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 5月18日.
 */
@EnableWebMvc
@Configuration
@ConditionalOnClass(BaseActionInterceptor.class)
@AutoConfigureAfter({BaseActionInterceptor.class,SpringContextUtil.class})
public class InterceptorAutoConfiguration extends WebMvcConfigurerAdapter {

    private static final String PATH_EXTENAL="/extenal/*";

    @Autowired(required = false)
    private List<BaseActionInterceptor> interceptors = new ArrayList<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration reg;
        Collections.sort(interceptors);
        for(BaseActionInterceptor interceptor :interceptors){
            reg = registry.addInterceptor(interceptor);
            if(interceptor.getClass().getAnnotation(PathPattern.class)!=null)
                reg.addPathPatterns(interceptor.getClass().getAnnotation(PathPattern.class).path());
            if(interceptor.getClass().getAnnotation(ExcludePathPattern.class)!=null)
                reg.excludePathPatterns(interceptor.getClass().getAnnotation(ExcludePathPattern.class).path());
        }
    }



}
