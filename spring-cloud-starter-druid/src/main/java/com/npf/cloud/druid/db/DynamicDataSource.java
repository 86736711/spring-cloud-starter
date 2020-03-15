package com.npf.cloud.druid.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @ProjectName: spring-cloud-starter-druid
 * @Package: com.npf.cloud.druid.db.aop
 * @ClassName: DynamicDataSource
 * @Author: ningpf
 * @Description: 动态数据源
 * @Date: 2019/7/3 0003 13:31
 * @Version: 1.0
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        //这的代码暂时没有用只是根据
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();

        DataSourceContent dataSource = DataSourceThreadHolder.getDataSource();

        if(log.isDebugEnabled()){
            log.debug("get dataSource:{}",dataSource!=null?dataSource.getBeanName():dataSource);
        }
        return dataSource!=null?dataSource.getBeanName():dataSource;
    }
}
