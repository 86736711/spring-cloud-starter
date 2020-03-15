package com.npf.cloud.framework.exception;

import com.npf.cloud.framework.common.model.MessageSourceBO;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.framework.exception
 * @ClassName: ApplicationException
 * @Author: ningpf
 * @Description: 这个是业务的作为阻断线程的执行返回的异常，不包含异常信息。只包含业务中返回的业务异常，如果不用国际化的异常
 * @Date: 2020/3/11 14:13
 * @Version: 1.0
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = -4832597104421398005L;

    public ApplicationException(String message) {
        super(message);
    }

}
