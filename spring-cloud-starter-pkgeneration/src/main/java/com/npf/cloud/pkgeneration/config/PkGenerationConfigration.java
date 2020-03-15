package com.npf.cloud.pkgeneration.config;

import com.npf.cloud.pkgeneration.DefaultKeyGenerator;
import com.npf.cloud.pkgeneration.KeyGenerator;
import com.npf.cloud.zookeeper.ZkClient;
import com.npf.cloud.zookeeper.config.ZkConfigration;
import com.npf.cloud.zookeeper.constant.ZkConstant;
import org.apache.zookeeper.CreateMode;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @Author: 宁培峰
 * @Description:
 * @Date: Created in 16:28 2018/8/23 0023
 * @Modified By
 */
@Configuration
@AutoConfigureAfter(ZkConfigration.class)
@EnableConfigurationProperties({PkGeneratorProperties.class})
public class PkGenerationConfigration {

    public static final String WORK_ID_PATH = "server/workid/";



    @Bean
    public KeyGenerator getKeyGenerator(PkGeneratorProperties pkGeneratorProperties, ZkClient zkClient){
        int workid = pkGeneratorProperties.getWorkid();

        try {
            do {
                workid = (new Random()).nextInt(1024) + 1;
            } while(zkClient.pathExist(zkClient.getPath(ZkConstant.zkRootPath,WORK_ID_PATH+workid)));

            zkClient.createPath(zkClient.getPath(ZkConstant.zkRootPath,WORK_ID_PATH+workid),CreateMode.EPHEMERAL);
        } catch (Exception e) {
        }



        DefaultKeyGenerator.setWorkerId(workid);
        return new DefaultKeyGenerator();
    }

}
