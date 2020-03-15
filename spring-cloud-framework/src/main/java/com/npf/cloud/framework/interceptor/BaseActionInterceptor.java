package com.npf.cloud.framework.interceptor;

import com.npf.cloud.framework.common.AbleSort;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.interceptor
 * @ClassName: BaseActionInterceptor
 * @Author: ningpf
 * @Description: 基础的过滤器
 * @Date: 2020/3/11 12:58
 * @Version: 1.0
 */
public abstract class BaseActionInterceptor extends HandlerInterceptorAdapter implements AbleSort {
}
