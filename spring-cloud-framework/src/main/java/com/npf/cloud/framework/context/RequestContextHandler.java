package com.npf.cloud.framework.context;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.context
 * @ClassName: RequestContextHandler
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/11 13:23
 * @Version: 1.0
 */
public class RequestContextHandler {

    private static ThreadLocal<RequestContext> context = new ThreadLocal<>();


    public static void setContext(RequestContext requestContext){
        context.set(requestContext);
    }


    public static RequestContext getContext(){
        return context.get();
    }


    public static void clean(){
        context.remove();
    }
}
