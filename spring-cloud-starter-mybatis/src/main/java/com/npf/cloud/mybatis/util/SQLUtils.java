package com.npf.cloud.mybatis.util;

import com.npf.cloud.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mybatis.util
 * @ClassName: SQLUtils
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/13 11:08
 * @Version: 1.0
 */
@Slf4j
@Component
public class SQLUtils {

    @Async
    public String print(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
        String fullSQL = getFullSQL(configuration, boundSql);
        log.debug("执行SQL: {}", "耗费:" + time + "毫秒:" + sqlId + ":" + fullSQL);

        return fullSQL;
    }

    /**
     * 功能描述：获取完整SQL，包含参数
     *
     * @param configuration 配置
     * @param boundSql      SQL参数对象
     * @return 带参数的SQL
     */
    private String getFullSQL(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    /**
     * <b>功能描述：</b>获取SQL参数<br>
     * <b>修订记录：</b><br>
     * <li>20180531&nbsp;&nbsp;|&nbsp;&nbsp;赵伟&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     *
     * @param obj 参数对象
     * @return 参数值
     */
    private String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return StringUtils.filterDollarStr(value);
    }
}
