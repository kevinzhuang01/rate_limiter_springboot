package com.example.demo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service

public class RatelimiterService {
    private final RedisTemplate<String, Integer> redisTemplate;
    private final int MAX_REQUESTS = 5;
    private final int WINDOW_SECONDS = 60;

    public RatelimiterService(RedisTemplate<String,Integer> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId){
        String key = "rate_limit:" + userId;
        Integer count = redisTemplate.opsForValue().get(key);

        // If client hasnt made a request yet, we give it a value of 60 seconds()
        // if we make 5 request in 60 seconds, we have wait until the 1st request expires and then another api request available 
        // exactly like sliding window..
        if (count == null){
            redisTemplate.opsForValue().set(key,1,WINDOW_SECONDS,TimeUnit.SECONDS);
        }

        // if our current requests doesn't exceed MAX_Requests
        if (count < MAX_REQUESTS){
            redisTemplate.opsForValue().increment(key);
            return true;
        }

        return false;
    }
}
