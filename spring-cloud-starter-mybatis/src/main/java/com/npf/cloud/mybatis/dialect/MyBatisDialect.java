package com.npf.cloud.mybatis.dialect;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mybatis.dialect
 * @ClassName: MyBatisDialect
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/13 11:24
 * @Version: 1.0
 */
public interface MyBatisDialect {

    /**
     * 功能描述：获取分页sql
     * @param start 开始行数
     * @param limit 每页大小
     * @param sql 原sql
     */
    String getLimitSql(Integer start, Integer limit, String sql);

    /**
     * 功能描述：获取总行数sql
     * @param sql 原sql
     */
    String getTotalSql(String sql);
}
