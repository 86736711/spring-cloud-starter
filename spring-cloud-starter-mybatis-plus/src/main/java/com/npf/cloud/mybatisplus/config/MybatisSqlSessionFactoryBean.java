package com.npf.cloud.mybatisplus.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: spring-cloud-starter
 * @Package: com.npf.cloud.mybatisplus.config
 * @ClassName: MybatisSqlSessionFactoryBean
 * @Author: ningpf
 * @Description: ${description}
 * @Date: 2020/3/14 14:43
 * @Version: 1.0
 */
public class MybatisSqlSessionFactoryBean extends SqlSessionFactoryBean {

    private static final Log logger = LogFactory.getLog(SqlSessionFactoryBean.class);

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    @Override
    public void   setTypeAliasesPackage(String typeAliasesPackage){
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        typeAliasesPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(typeAliasesPackage) + "/" + DEFAULT_RESOURCE_PATTERN;

        try {
            List<String> result = new ArrayList<>();
            Resource[] resources =  resolver.getResources(typeAliasesPackage);
            if(resources != null && resources.length > 0){
                MetadataReader metadataReader ;
                for(Resource resource : resources){
                    if(resource.isReadable()){
                        metadataReader =  metadataReaderFactory.getMetadataReader(resource);
                        try {
                            result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName());
                        } catch (ClassNotFoundException e) {
                            logger.warn("mybatis type aliases package class match error!",e);
                        }
                    }
                }
            }
            if(result.size() > 0) {
                super.setTypeAliasesPackage(StringUtils.join(result.toArray(), ","));
            }else{
                logger.warn("no mybatis type aliases package matched :"+typeAliasesPackage);
            }
        } catch (Exception e) {
            logger.error("mybatis type aliases package faild :"+ typeAliasesPackage,e);
        }
    }
}
