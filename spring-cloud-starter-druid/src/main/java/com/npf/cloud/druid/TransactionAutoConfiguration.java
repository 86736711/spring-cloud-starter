package com.npf.cloud.druid;

import com.npf.cloud.druid.db.DynamicDataSource;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.druid
 * @ClassName: TransactionAutoConfiguration
 * @Author: ningpf
 * @Description: druid的事务配置
 * @Date: 2020/3/11 17:26
 * @Version: 1.0
 */
@Configuration
@AutoConfigureAfter(DruidAutoConfiguration.class)
@EnableTransactionManagement
public class TransactionAutoConfiguration implements TransactionManagementConfigurer {

    //数据源
    private final DataSource dynamicDataSource;

    @Autowired
    public TransactionAutoConfiguration(DynamicDataSource dynamicDataSource) {
        this.dynamicDataSource = dynamicDataSource;
    }


    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource);
    }



    @Bean
    public TransactionInterceptor transactionInterceptor() {
        TransactionInterceptor ti = new TransactionInterceptor();
        ti.setTransactionManager(annotationDrivenTransactionManager());
        Properties properties = new Properties();
        properties.setProperty("find*", "PROPAGATION_REQUIRED, readOnly");
        properties.setProperty("get*", "PROPAGATION_REQUIRED, readOnly");
        properties.setProperty("select*", "PROPAGATION_REQUIRED, readOnly");
        properties.setProperty("query*", "PROPAGATION_REQUIRED, readOnly");
        properties.setProperty("insert*", "PROPAGATION_REQUIRED");
        properties.setProperty("save*", "PROPAGATION_REQUIRED");
        properties.setProperty("add*", "PROPAGATION_REQUIRED");
        properties.setProperty("delete*", "PROPAGATION_REQUIRED");
        properties.setProperty("update*", "PROPAGATION_REQUIRED");
        properties.setProperty("dispose*", "PROPAGATION_REQUIRED,+OrderNoFallbackRuntimeException");
        properties.setProperty("sync*", "PROPAGATION_REQUIRED");
        properties.setProperty("upload*", "PROPAGATION_REQUIRED");
        properties.setProperty("clear*", "PROPAGATION_REQUIRED");
        ti.setTransactionAttributes(properties);
        return ti;
    }



    /**
     * @description 利用AspectJExpressionPointcut设置切面
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        // 声明切点要切入的面
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // 设置需要被拦截的路径
        pointcut.setExpression("execution(* cn.com.tcsl.s1..*.service..*ServiceImpl.*(..))");
        // 设置切面和配置好的事务管理
        return new DefaultPointcutAdvisor(pointcut, transactionInterceptor());
    }

}
