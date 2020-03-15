package com.npf.cloud.zookeeper.entity;

/**
 * @ProjectName: tcsl-smart-starter
 * @Package: cn.com.tcsl.s1.starter.zookeeper.entity
 * @ClassName: RetryTypeEnum
 * @Author: ningpf
 * @Description: 重试类型的枚举
 * @Date: 2019/7/6 0006 11:56
 * @Version: 1.0
 */
public enum RetryTypeEnum {
    EXPONENTIAL_BACKOFF_RETRY("exponentialBackoffRetry"),
    BOUNDED_EXPONENTIAL_BACKOFF_RETRY("boundedExponentialBackoffRetry"),
    RETRY_NTIMES("retryNTimes"),
    RETRY_FOREVER("retryForever"),
    RETRY_UNTIL_ELAPSED("retryUntilElapsed");

    private String value;

    private RetryTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RetryTypeEnum fromString(String value) {
        for (RetryTypeEnum type : RetryTypeEnum.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
