package com.npf.cloud.druid.db;

/**
 * @ProjectName: spring-cloud-starter-druid
 * @Package: com.npf.cloud.druid.db.aop
 * @ClassName: DataSourceThreadHolder
 * @Author: ningpf
 * @Description: 线程存储的数据源，仅线程可见
 * @Date: 2019/7/3 0003 13:36
 * @Version: 1.0
 */
public class DataSourceThreadHolder {


    private static ThreadLocal<DataSourceContent> threadlocal = new ThreadLocal<DataSourceContent>();

    public static DataSourceContent getDataSource() {
        return threadlocal.get();
    }

    public static void setDataSource(DataSourceContent dataSourceBeanBuilder) {
        threadlocal.set(dataSourceBeanBuilder);
    }

    public static void clearDataSource() {
        threadlocal.remove();
    }

}
