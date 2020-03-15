package com.npf.cloud.zookeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ProjectName: tcsl-smart-starter
 * @Package: cn.com.tcsl.s1.starter.zookeeper.config
 * @ClassName: ZkProperties
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2019/7/6 0006 11:42
 * @Version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "s1.zk")
public class ZkProperties {

    private String connectString;

    private int sessionTimeoutMs = 15000;

    private int connectionTimeoutMs = 15000;

    private String retryType = "retryNTimes";

    private int exponentialBackoffRetryBaseSleepTimeMs = 2000;

    private int exponentialBackoffRetryMaxRetries = 10;

    private int boundedExponentialBackoffRetryBaseSleepTimeMs = 2000;

    private int boundedExponentialBackoffRetryMaxSleepTimeMs = 60000;

    private int boundedExponentialBackoffRetryMaxRetries = 10;

    private int retryNTimesCount = 10;

    private int retryNTimesSleepMsBetweenRetries = 2000;

    private int retryForeverRetryIntervalMs = 1000;

    private int retryUntilElapsedMaxElapsedTimeMs = 60000;

    private int retryUntilElapsedSleepMsBetweenRetries = 2000;




}
