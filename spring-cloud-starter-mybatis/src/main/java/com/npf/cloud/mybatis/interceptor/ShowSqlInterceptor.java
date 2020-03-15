package com.npf.cloud.mybatis.interceptor;

import com.npf.cloud.mybatis.util.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mybatis.interceptor
 * @ClassName: ShowSqlInterceptor
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/13 11:05
 * @Version: 1.0
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
@Component
public class ShowSqlInterceptor implements Interceptor {

    @Resource
    private SQLUtils sqlUtils;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Object paramObj = boundSql.getParameterObject();
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnValue;

        if (paramObj instanceof Map) {
            Map<String, Object> param = (Map<String, Object>) paramObj;
            if (param.containsKey("start") && param.containsKey("limit")) {
                return invocation.proceed();
            }
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        returnValue = invocation.proceed();
        stopWatch.stop();
        long time = stopWatch.getTime();

        if (time > 1) {
            sqlUtils.print(configuration, boundSql, sqlId, time);
        }

        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
