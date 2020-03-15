package com.npf.cloud.zookeeper.config;

import cn.com.tcsl.s1.starter.zookeeper.ZkClient;
import cn.com.tcsl.s1.starter.zookeeper.ZkClientImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: tcsl-smart-starter
 * @Package: cn.com.tcsl.s1.starter.zookeeper.config
 * @ClassName: ZkConfigration
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2019/7/6 0006 13:07
 * @Version: 1.0
 */
@Configuration
@EnableConfigurationProperties({ZkProperties.class})
public class ZkConfigration {

    @Bean
    public ZkClient zkClient(){
        return new ZkClientImpl();
    }
}
