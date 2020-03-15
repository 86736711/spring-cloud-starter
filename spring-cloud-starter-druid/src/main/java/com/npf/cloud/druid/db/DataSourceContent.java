package com.npf.cloud.druid.db;

import lombok.Getter;

/**
 * @ProjectName: spring-cloud-starter-druid
 * @Package: com.npf.cloud.druid.db.aop
 * @ClassName: DataSourceContent
 * @Author: ningpf
 * @Description: 数据源信息，这里的数据源对形象的值是不能修改的
 * @Date: 2019/7/3 0003 13:38
 * @Version: 1.0
 */
@Getter
public class DataSourceContent {

    private String beanName; // 注册到spring的bean


    private DataSourceContent(){}

    public DataSourceContent(String beanName){
        this.beanName = beanName;
    }

}
