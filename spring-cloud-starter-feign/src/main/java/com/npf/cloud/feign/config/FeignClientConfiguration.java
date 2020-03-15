package com.npf.cloud.feign.config;

import com.npf.cloud.framework.context.RequestContext;
import com.npf.cloud.framework.context.RequestContextHandler;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.feign.config
 * @ClassName: FeignClientConfiguration
 * @Author: ningpf
 * @Description: feign配合
 * @Date: 2020/3/15 16:20
 * @Version: 1.0
 */
@Configuration
@ConditionalOnClass(RequestContextHandler.class)
public class FeignClientConfiguration {

    //可以自定义这个header
    public static final String HEADER_TOKEN = "Access-Token-Shop";

    @Bean
    public RequestInterceptor headerTokenInterceptor() {
        return new RequestInterceptor() {

            @Override
            public void apply(RequestTemplate requestTemplate) {


                RequestContext context = RequestContextHandler.getContext();

                if(context == null){
                    return;
                }

                HttpServletRequest request = context.getRequest();

                requestTemplate.header(HEADER_TOKEN, request.getHeader(HEADER_TOKEN)!=null?request.getHeader(HEADER_TOKEN):"");




            }




        };
    }
}
