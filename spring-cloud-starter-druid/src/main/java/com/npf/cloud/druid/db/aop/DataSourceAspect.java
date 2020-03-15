package com.npf.cloud.druid.db.aop;

import com.npf.cloud.druid.db.DataSourceContent;
import com.npf.cloud.druid.db.DataSourceThreadHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Order(0)
public class DataSourceAspect {


	@Pointcut(value = "@annotation(com.npf.cloud.druid.db.aop.DataSource)")
    private void pointcut() {
    }
	
	/**
	 * 数据源切换注解前处理
	 * 
	 * @version v1.0
	 * @author litie
	 */
	@Before(value = "pointcut() && @annotation(dataSource)")
	public void before(JoinPoint point, DataSource dataSource) throws Throwable {

		DataSourceContent dataSourceContent = null;

		if(DbConstant.DB_TYPE_R.equals(dataSource.dsType())){
			dataSourceContent = new DataSourceContent(DbConstant.getBeanDataSourceName(DbConstant.DB_TYPE_R));
		}else{
			dataSourceContent = new DataSourceContent(DbConstant.getBeanDataSourceName(DbConstant.DB_TYPE_RW));
		}

		DataSourceThreadHolder.setDataSource(dataSourceContent);
	}
	
	/**
	 * 数据源切换注解后处理
	 * 
	 * @version v1.0
	 * @author litie
	 */
	@After(value = "pointcut()")
    public void after() {
		DataSourceThreadHolder.clearDataSource();
    }
}
