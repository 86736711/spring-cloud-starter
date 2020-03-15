package com.npf.cloud.pkgeneration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: 宁培峰
 * @Description:
 * @Date: Created in 16:23 2018/8/23 0023
 * @Modified By
 */
@ConfigurationProperties(prefix = "s1.pk")
public class PkGeneratorProperties {


    private int workid;

    public int getWorkid() {
        return workid;
    }

    public void setWorkid(int workid) {
        this.workid = workid;
    }


}
