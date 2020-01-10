package com.suntech.feo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.config
 * @Description : 缓存的启动配置类
 * @Author : chenlei
 * @Create Date : 2020年01月03日 10:53
 * ------------ -------------- ---------------------------------
 */
@SpringBootApplication(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class ModuleFrameworkCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleFrameworkCacheApplication.class, args);
    }

}
