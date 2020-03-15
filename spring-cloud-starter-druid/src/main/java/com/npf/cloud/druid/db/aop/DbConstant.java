package com.npf.cloud.druid.db.aop;

/**
 * @ProjectName: spring-cloud-starter-druid
 * @Package: com.npf.cloud.druid.db.aop
 * @ClassName: DbConstant
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2019/7/3 0003 14:50
 * @Version: 1.0
 */
public class DbConstant {

    //这个字段和Spring中的beanid的前缀一样，所以多数据源的beanid的规则得一致
    public static final String DB_SOURCE_NAME = "dataSource";
    public static final String DB_TYPE_RW = "master";
    public static final String DB_TYPE_R = "slave";

    public static String getBeanDataSourceName(String dbType){
        return DB_SOURCE_NAME + "_" + dbType;
    }

}
