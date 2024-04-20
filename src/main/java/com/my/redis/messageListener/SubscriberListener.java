package com.my.redis.messageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.UnsupportedEncodingException;

public class SubscriberListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(SubscriberListener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] channel = message.getChannel();
        byte[] body = message.getBody();
        try {
            String content = new String(body, "UTF-8");
            String address = new String(channel, "UTF-8");
            logger.info("收到頻道[{}]消息：{}", address, content);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
