package com.my.redis.config;

import com.my.redis.service.IRedisService;
import com.my.redis.service.com.my.redis.service.provider.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisServiceConfig {
    /**
     * 定義redisService
     * @return
     */
    @Bean(name = "redisService" )
    public IRedisService redisService() {
        return new RedisService();
    }
}
