package com.my.redis.config;

import com.my.redis.messageListener.SubscriberListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisMessageConfig {

    @Autowired(required = true)
    @Qualifier(value = "jedisConnectionFactory")
    protected JedisConnectionFactory jedisConnectionFactory;

    /**
     * PUB/SUB使用
     * @return
     */
    @Bean
    public RedisMessageListenerContainer listenerContainer() {
        // 創建RedisMessageListenerContainer對象
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 設置 RedisConnection 工廠，多種 JavaRedis客戶端接入工廠
        container.setConnectionFactory(jedisConnectionFactory);
        // 添加監聽器
        container.addMessageListener(new SubscriberListener(), new ChannelTopic("demoChannel"));
        return container;
    }

}
