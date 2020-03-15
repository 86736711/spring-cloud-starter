package com.npf.cloud.framework.interceptor.annotation;

import java.lang.annotation.*;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.interceptor.annotation
 * @ClassName: PathPattern
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/11 13:29
 * @Version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathPattern {
    String[] path()default "/**";
}
