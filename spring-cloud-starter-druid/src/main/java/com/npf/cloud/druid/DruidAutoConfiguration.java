
package com.npf.cloud.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.npf.cloud.druid.db.DynamicDataSource;
import com.npf.cloud.mybatisplus.config.MybatisPlusConfig;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.util.HashMap;


/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.druid
 * @ClassName: DruidAutoConfiguration
 * @Author: ningpf
 * @Description: druid的配置
 * @Date: 2020/3/11 17:26
 * @Version: 1.0
 */
@Configuration
@AutoConfigureBefore(MybatisPlusConfig.class)
public class DruidAutoConfiguration {

    @Value("${spring.cloud.config.profile}")
    private String profileName;


   /**
    * @Method dataSource_master
    * @Author ningpf
    * @Version  1.0
    * @Description 主数据源，可读写
    * @Return
    * @Exception
    * @Date 2019/7/3 0003 21:40
    */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource_master() {
        return new DruidDataSource();
    }

    /**
     * @Method dataSource_master
     * @Author ningpf
     * @Version  1.0
     * @Description 主数据源，可读写
     * @Return
     * @Exception
     * @Date 2019/7/3 0003 21:40
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource dataSource_slave() {
        return new DruidDataSource();
    }



    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource(DataSource dataSource_master, DataSource dataSource_slave) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(new HashMap(){{
            put("dataSource_master",dataSource_master);
            put("dataSource_slave",dataSource_slave);
        }});
        dynamicDataSource.setDefaultTargetDataSource(dataSource_master);
        return dynamicDataSource;
    }



    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        // org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
                new StatViewServlet(), "/"+profileName+"druid/*");

        servletRegistrationBean.addInitParameter("slowSqlMillis", "1");
        servletRegistrationBean.addInitParameter("logSlowSql", "true");
        // IP白名单 (没有配置或者为空，则允许所有访问)
        //servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not
        // permitted to view this page.
        //servletRegistrationBean.addInitParameter("deny", "192.168.1.73");
        // 登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        // 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }



    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(
                new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");// 添加过滤规则
        // 添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions",
                "*.js,*.gif,*.jpg,*.png,*.css,*.ico,"+"/"+profileName+"druid/*");
        return filterRegistrationBean;
    }


    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        DruidStatInterceptor dsInterceptor = new DruidStatInterceptor();
        return dsInterceptor;
    }

    @Bean
    @Scope("prototype")
    public JdkRegexpMethodPointcut druidStatPointcut() {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPattern("cn.com.tcsl.s1.*.service.*");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor druidStatAdvisor(DruidStatInterceptor druidStatInterceptor, JdkRegexpMethodPointcut druidStatPointcut) {
        DefaultPointcutAdvisor defaultPointAdvisor = new DefaultPointcutAdvisor();
        defaultPointAdvisor.setPointcut(druidStatPointcut);
        defaultPointAdvisor.setAdvice(druidStatInterceptor);
        return defaultPointAdvisor;
    }



}
