package com.npf.cloud.redisson;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ProjectName: spring-cloud-starter-redisson
 * @Package: com.npf.cloud.redisson.queue
 * @ClassName: RedissonProperties
 * @Author: ningpf
 * @Description: redisson的配置
 * @Date: 2020/3/15 15:34
 * @Version: 1.0
 */
@ConfigurationProperties(prefix = "spring.redisson")
public class RedissonProperties {

    private String password;

    private String clientName;

    private String address;

    private Integer database;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }
}
