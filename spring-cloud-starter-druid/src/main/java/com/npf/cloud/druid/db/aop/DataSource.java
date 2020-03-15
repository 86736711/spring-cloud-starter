package com.npf.cloud.druid.db.aop;

import java.lang.annotation.*;


@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {

	String dsType() default DbConstant.DB_TYPE_R;

}
