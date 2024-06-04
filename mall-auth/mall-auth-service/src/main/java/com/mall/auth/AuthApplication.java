package com.mall.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : [lm]
 * @version : [v1.0]
 * @createTime : [2024/3/29 21:05]
 */
@SpringBootApplication
@MapperScan("com.mall.auth.mapper")
@EnableFeignClients(basePackages = "com.mall.api.client")
@EnableScheduling
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
