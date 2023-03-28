package com.example.order_online.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 16537
 * @Classname RedisUtil
 * @Description
 * @Version 1.0.0
 * @Date 2023/1/15 11:23
 */
@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    public Map<Object, Object> hEntries(String key){
        return redisTemplate.opsForHash().entries(key);

    }
    public void vSet(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }
    public void vSet(String key, Object value, long time, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,time,timeUnit);
    }
    public Object vGet(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean setEx(String key, long time, TimeUnit timeUnit){
        return redisTemplate.expire(key, time, timeUnit);

    }
    public Boolean vSetIfAbsent(String key,Object value){
        return redisTemplate.opsForValue().setIfAbsent(key,value);
    }
    public Boolean vSetIfAbsent(String key, Object value, long time,TimeUnit timeUnit){
        return redisTemplate.opsForValue().setIfAbsent(key,value,time,timeUnit);
    }
    public Boolean vSetIfPresent(String key,Object value){
        return redisTemplate.opsForValue().setIfAbsent(key,value);
    }
    public Boolean vSetIfPresent(String key, Object value, long time,TimeUnit timeUnit){
        return redisTemplate.opsForValue().setIfAbsent(key,value,time,timeUnit);
    }
    public Set<String> keys(String pattern){
        return stringRedisTemplate.keys(pattern);
    }
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }
    public Boolean delete(String key){
        return redisTemplate.delete(key);
    }
    public Long hDelete(String key,String hashKey){
        return redisTemplate.opsForHash().delete(key,hashKey);
    }
    public Long delete(Collection<String> keys){
        return redisTemplate.delete(keys);
    }
    public Long getExpire(String key){
        return redisTemplate.getExpire(key);
    }
    public Long getExpire(String key,TimeUnit timeUnit){
        return redisTemplate.getExpire(key,timeUnit);
    }
    public Long increment(String key){
        return redisTemplate.opsForValue().increment(key);
    }
    public Object hGet(String key, String hashKey){
        return redisTemplate.opsForHash().get(key,hashKey);
    }

    public void hSet(String key,Object hashKey,Object value) {
        redisTemplate.opsForHash().put(key,hashKey,value);
    }

    public void hIncrement(String key, String goodsId,Long inc) {
        redisTemplate.opsForHash().increment(key,goodsId,inc);
    }
}
