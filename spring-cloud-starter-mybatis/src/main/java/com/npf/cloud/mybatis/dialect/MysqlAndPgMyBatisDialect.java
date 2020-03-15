package com.npf.cloud.mybatis.dialect;

import org.springframework.stereotype.Component;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mybatis.dialect
 * @ClassName: MysqlAndPgMyBatisDialect
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/13 11:27
 * @Version: 1.0
 */
@Component
public class MysqlAndPgMyBatisDialect implements MyBatisDialect {

    @Override
    public String getLimitSql(Integer start, Integer limit, String sql) {

        StringBuilder sb = new StringBuilder( sql.length()+20 );
        sb.append( sql );

        if( start>0 ){
            sb.append(" limit ").append(limit).append(" offset ").append(start);
        } else {
            sb.append(" limit ").append(limit);
        }

        return sb.toString();
    }

    @Override
    public String getTotalSql(String sql) {
        return "select count(1) from (" + sql + ") total";
    }
}
