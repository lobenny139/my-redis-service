package com.my.redis.service.com.my.redis.service.provider;

import com.my.redis.service.IRedisService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Component
@Service
public class RedisService implements IRedisService {
    public static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired(required = true)
    @Qualifier(value = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    protected int getDatabase(){
        return ( (JedisConnectionFactory)this.getRedisTemplate().getConnectionFactory() ).getDatabase();
    }

    protected void setDatabase(int db){
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory)this.getRedisTemplate().getConnectionFactory();
        jedisConnectionFactory.setDatabase(db);
        jedisConnectionFactory.afterPropertiesSet();
    }

    @Override
    public Object get(int db, String key) {
        try {
            if (key == null) {
                throw new RuntimeException("鍵值不能為空");
            }
            setDatabase(db);
            Object value = getRedisTemplate().opsForValue().get(key);
            if (value != null) {
                logger.info("成功從Redis[{}]取得鍵值[{}]", getDatabase(), key);
            } else {
                logger.warn("鍵[{}]不存在於Redis[{}]", key, getDatabase());
            }
            return value;
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public Object get(String key) {
        return get(0, key);
    }

    public boolean set( int db, String key, Object value, long time)  {
        if(key == null){throw new RuntimeException("鍵不能為空");}
        try {
            setDatabase(db);
            if (time > 0) {
                getRedisTemplate().opsForValue().set(key, value, time, TimeUnit.SECONDS);
                logger.info("鍵[{}]的有效期為 {} 秒後", key, time);
            }else{
                getRedisTemplate().opsForValue().set(key, value);
            }
            logger.info("成功放入[{}:{}]到Redis[{}]", key, value, getDatabase());
            return true;
        } catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        } catch (Exception e) {
            logger.warn("不能放入[{}:{}]到Redis[{}]", key, value, getDatabase());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean set( String key, Object value, long time)  {
        return set(0, key, value, time);
    }

    @Override
    public boolean set(String key, Object value) {
        return set(0, key, value, 0);
    }

    public boolean del(int db,  String... keys) {
        try {
            setDatabase(db);
            if (keys != null && keys.length > 0) {
                getRedisTemplate().delete(Arrays.asList(keys));
                logger.info("成功從Redis[{}]中刪除鍵{}", getDatabase(), Arrays.asList(keys) );
                return true;
            }else{
                throw new RuntimeException("鍵不能為空");
            }
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean del(String... keys) {
        return this.del(0, keys);
    }

    @Override
    public List<String> getAllKeys(int db, String keyPreifx){
        try {
            setDatabase(db);
            StringBuffer targetKey = new StringBuffer();
            if (keyPreifx != null) {
                targetKey.append(keyPreifx.trim());
            }
            targetKey.append("*");

            Set<String> redisKeys = getRedisTemplate().keys(targetKey.toString());
            // Store the keys in a List
            List<String> keysList = new ArrayList<>();
            Iterator<String> it = redisKeys.iterator();
            while (it.hasNext()) {
                String data = it.next();
                keysList.add(data);
            }
            logger.info("成功從Redis[{}]中取得取得鍵(prefix={}), 共{}筆記錄.", getDatabase(), targetKey, redisKeys.size() );
            return keysList;
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllKeys(String keyPreifx){
        return getAllKeys(0, keyPreifx);
    }

    @Override
    public boolean hasKey(int db, String key) {
        try{
            setDatabase(db);
            return getRedisTemplate().hasKey(key);
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasKey(String key) {
        return hasKey(0, key);
    }

    public  List<String> getAllKeys(int db){
        return getAllKeys(db, null);
    }

    public List<String> getAllKeys(){
        return getAllKeys(0);
    }


    @Override
    public long incr(int db, String key, long delta) {
        try{
            setDatabase(db);
            long result = getRedisTemplate().opsForValue().increment(key, delta);
            logger.info("成功從Redis[{}]中以鍵值[{}]進行遞增, 遞增後的值為{}.", getDatabase(), key, result );
            return result;
        }catch(RedisConnectionFailureException e){
            throw new RuntimeException("無法連接到redis服務器, " + e.toString());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public long incr(String key, long delta){
        return incr(0, key, delta);
    }

    @Override
    public long incr(int db, String key){
        return incr(db, key, 1);
    }

    @Override
    public long incr(String key){
        return incr(0, key, 1);
    }


    @Override
    public boolean publish(String topic, String message) {
        try {
            getRedisTemplate().convertAndSend(topic, message);
            return true;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
