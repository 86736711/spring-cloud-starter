package com.npf.cloud.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.swagger
 * @ClassName: SwaggerConfig
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/15 15:59
 * @Version: 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {



    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${system.test:false}")
    private Boolean isTest;

    @Bean
    public Docket createRestAPI(){
        return new Docket(DocumentationType.SWAGGER_2)
                // 生产环境的时候关闭 swagger 比较安全，可配置化
                .enable(isTest)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.com.tcsl.s1"))
//                .apis(RequestHandlerSelectors.withClassAnnotation(ApiOperation.class))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                //在接口文档自动生成的demo数据格式，否则LocalDateTime等会显示为对象格式
                .directModelSubstitute(LocalDateTime.class, Date.class)
                .directModelSubstitute(OffsetDateTime.class, Date.class)
                .directModelSubstitute(LocalDate.class, Date.class)
                .directModelSubstitute(LocalTime.class, Date.class)
                .directModelSubstitute(ZonedDateTime.class, Date.class)
                ;
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("Access-Token-Shop", "Access-Token-Shop", "header"));
        apiKeyList.add(new ApiKey("Access-Token-User", "Access-Token-User", "header"));
        apiKeyList.add(new ApiKey("app-type", "app-type", "header"));
        return apiKeyList;
    }
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts=new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences=new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    private ApiInfo apiInfo(){

        return new ApiInfoBuilder()
                .title("微服务接口管理系统")
                .description(applicationName+"服务接口文档，会持续维护完善")
                .termsOfServiceUrl(applicationName+"服务")
                .contact("研发团队")
                .version("v1.1.0")
                .build()
                ;
    }
}
