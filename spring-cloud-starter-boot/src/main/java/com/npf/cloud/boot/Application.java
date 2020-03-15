package com.npf.cloud.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
扫描是根据使用的项目重写
feign的调用也根据业务的包重写
 */

@SpringBootApplication
@PropertySource("classpath:config/bootstrap.properties")
@ComponentScan(basePackages = "com.npf")
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableFeignClients(basePackages = "com.npf")
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		application.run(args);
	}

}