package com.npf.cloud.framework.exception;

/**
 * @ProjectName: tcsl-smart-starter
 * @Package: cn.com.tcsl.s1.framework.exception
 * @ClassName: ZkException
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2019/7/6 0006 12:29
 * @Version: 1.0
 */
public class ZkException extends RuntimeException {

    private static final long serialVersionUID = -2413343179708080677L;

    public ZkException() {
        super();
    }

    public ZkException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZkException(String message) {
        super(message);
    }

    public ZkException(Throwable cause) {
        super(cause);
    }
}
