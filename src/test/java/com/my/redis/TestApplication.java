package com.my.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@ComponentScan(basePackages = {
        "com.my.redis.service.provider"
})

@Import({
        com.my.redis.config.RedisConfig.class,
        com.my.redis.config.RedisServiceConfig.class,
        com.my.redis.config.RedisMessageConfig.class
})
@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
