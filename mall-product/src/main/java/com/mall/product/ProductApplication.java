package com.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 商品服务入口
 *
 * @author lm
 * @since 2024-06-12 15:31:41
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("com.mall.product.mapper")
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
