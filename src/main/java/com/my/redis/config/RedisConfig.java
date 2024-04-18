package com.my.redis.config;

import com.my.redis.service.IRedisService;
import com.my.redis.service.com.my.redis.service.provider.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    /*
     * Redis连接工厂(redis db=0)
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setDatabase(0);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    /*
     * 配置redisTemplate针对不同key和value场景下不同序列化的方式
     * @return
     */
    @Primary
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate0( ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //redis db=0
        template.setConnectionFactory( jedisConnectionFactory() );
        //
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);

        // 开启事务 true 不会主动关闭连接, false -> 主动关闭连接
        // https://www.jianshu.com/p/c9f5718e58f0
        template.setEnableTransactionSupport( false );

        template.afterPropertiesSet();
        return template;
    }


}
