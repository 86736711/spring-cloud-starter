package com.npf.cloud.mybatis.interceptor;

import com.npf.cloud.mybatis.dialect.MyBatisDialect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mybatis.interceptor
 * @ClassName: PagingInterceptor
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/13 11:20
 * @Version: 1.0
 */
@Slf4j
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})
})
@Component
public class PagingInterceptor implements Interceptor {

    /**
     * 数据库方言
     */
    @Resource
    private MyBatisDialect dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MappedStatement mappedStatement = null;

        Field delegateField = ReflectionUtils.findField(statementHandler.getClass(), "delegate");
        if (delegateField != null) {
            delegateField.setAccessible(true);

            PreparedStatementHandler delegate = (PreparedStatementHandler)
                    ReflectionUtils.getField(delegateField, statementHandler);
            Field mappedStatementField = ReflectionUtils.findField(delegate.getClass(), "mappedStatement");
            if (mappedStatementField != null) {
                mappedStatementField.setAccessible(true);

                mappedStatement = (MappedStatement) ReflectionUtils.getField(mappedStatementField, delegate);
            }
        }

        //Object mappedStatement = ReflectionUtils.getProperty(delegate, "mappedStatement");
        //((PreparedStatementHandler) ((RoutingStatementHandler) statementHandler).delegate).mappedStatement;
        BoundSql boundSql = statementHandler.getBoundSql();

        Object paramObj = boundSql.getParameterObject();

        if (paramObj instanceof Map) {
            //TODO param name config
            Map<String, Object> param = (Map<String, Object>) paramObj;
            if (param.containsKey("start") && param.containsKey("limit")) {
                Integer start = (Integer) param.get("start");
                Integer limit = (Integer) param.get("limit");
                String countSQL = (String) param.get("countSQL");

                if (start != null && limit != null) {
                    MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,
                            SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
                    String sql = boundSql.getSql();

                    //动态控制分页显示 优先查询总条数

                    //重写param参数, 加入total
                    //MappedStatement mappedStatement = (MappedStatement)metaStatementHandler.getValue("delegate.mappedStatement");
                    Connection connection = (Connection) invocation.getArgs()[0];

                    if (!param.containsKey("total")) {
                        if (!StringUtils.isEmpty(countSQL)) {
                            setPageParameter(countSQL, connection, mappedStatement, param);
                        } else {
                            setPageParameter(sql, connection, statementHandler, mappedStatement, param);
                        }

                    }

                    //获取总条数
                    Integer count = (Integer) param.get("total");
                    Integer countPage = (count + limit - 1) / limit;
                    Integer showCount = limit * (countPage - 1);
                    //					if (start >= showCount) {
                    //						start = showCount;
                    //					}
                    /*
                    分页sql:
                    i,存在定义的countSql的话就用定义的总数查询sql,
                    ii,如果没有定义这样的sql,就使用原始的分页sql
                     */
                    String pageSql = dialect.getLimitSql(start, limit, sql);
                    log.debug("封装分页sql:" + pageSql);
                    metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);


                }
            }
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object obj = invocation.proceed();
        stopWatch.stop();
        long time = stopWatch.getTime();
        if (time > 1) {
            if (log.isDebugEnabled()) {
                log.debug("执行分页后SQ用时:{}", time);
            }

            //            if (time > 1000) {
            //                Shop loginShop = null;
            //                try {
            //                    loginShop = getLoginShopContext().getLoginShop();
            //                } catch (Exception ignored) {
            //
            //                }
            //
            //                if (mappedStatement != null &&
            //                        !mappedStatement.getId().startsWith("cn.com.tcsl.spos1.dao.biz
            // .BizErrorLogMapper.insert")) {
            //                    getBizErrorLogger().log(String.valueOf(time), BizErrorLog.TYPE_SQL_SLOWLY,
            //                            mappedStatement.getId(), boundSql.getSql(),
            // loginShop != null ? loginShop.getId() : 0);
            //                }
            //            }
        }
        return obj;
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    /**
     * <b>功能描述：</b>为参数设置总行数<br>
     * <b>修订记录：</b><br>
     * <li>20131009&nbsp;&nbsp;|&nbsp;&nbsp;刘庆魁&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li>
     * <li>20140520&nbsp;&nbsp;|&nbsp;&nbsp;刘庆魁&nbsp;&nbsp;|&nbsp;&nbsp;调整为更为通用的参数设置方法, 解决list参数无法在in中拼接的问题</li>
     *
     * @param sql              要执行的源sql, 根据此sql将其转化为获取总行数的sql
     * @param statementHandler mybatis负责设置statement的对象
     * @param param            外部传入的参数map, 会将总行数信息以total为key写进去
     */
    private void setPageParameter(String sql, Connection connection, StatementHandler statementHandler,
                                  MappedStatement mappedStatement, Map<String, Object> param) {
        // 记录总记录数
        String countSql = dialect.getTotalSql(sql);
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            countStmt = connection.prepareStatement(countSql);

            statementHandler.parameterize(countStmt);//mybatis处理参数的方式

            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }

            param.put("total", totalCount);

            stopWatch.stop();
            long time = stopWatch.getTime();
            log.debug("执行SQL:耗费:{}毫秒:{}", time, countSql);

            //            if (time > 1000) {
            //                Shop loginShop = null;
            //                try {
            //                    loginShop = getLoginShopContext().getLoginShop();
            //                } catch (Exception ignored) {
            //
            //                }
            //
            //                if (!mappedStatement.getId().startsWith("cn.com.tcsl.spos1.dao.biz.BizErrorLogMapper
            // .insert")) {
            //                    getBizErrorLogger().log(String.valueOf(time), BizErrorLog.TYPE_SQL_SLOWLY,
            //                            mappedStatement.getId(), sql, loginShop != null ? loginShop.getId() : 0);
            //                }
            //            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(countStmt);
        }
    }

    /**
     * <b>功能描述：</b>对于已经有完整sql的查询不使用预编译执行sql<br>
     * <b>修订记录：</b>2017-09-12 陈锐<br>
     */
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
                                  Map<String, Object> param) {
        // 记录总记录数
        Statement countStmt = null;
        ResultSet rs = null;
        try {

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            countStmt = connection.createStatement();
            rs = countStmt.executeQuery(sql);
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }

            param.put("total", totalCount);

            stopWatch.stop();
            long time = stopWatch.getTime();
            log.debug("执行countSQL:耗费:{}毫秒:{}", time, sql);

            //            if (time > 1000) {
            //                Shop loginShop = null;
            //                try {
            //                    loginShop = getLoginShopContext().getLoginShop();
            //                } catch (Exception ignored) {
            //
            //                }
            //
            //                if (!mappedStatement.getId().startsWith("cn.com.tcsl.spos1.dao.biz.BizErrorLogMapper
            // .insert")) {
            //                    getBizErrorLogger().log(String.valueOf(time), BizErrorLog.TYPE_SQL_SLOWLY,
            //                            mappedStatement.getId(), sql, loginShop != null ? loginShop.getId() : 0);
            //                }
            //            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(countStmt);
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
