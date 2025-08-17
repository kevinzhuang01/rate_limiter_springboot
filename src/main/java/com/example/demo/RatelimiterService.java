package com.example.demo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service

public class RatelimiterService {
    private final RedisTemplate<String, String> redisTemplate;
    private final int MAX_REQUESTS = 5;
    private final int WINDOW_SECONDS = 60;

    public RatelimiterService(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId){
        String key = "rate_limit:" + userId;
        Long count = redisTemplate.opsForValue().increment(key);

        // If client hasnt made a request yet, we give it a value of 60 seconds()
        // if we make 5 request in 60 seconds, we have wait until the 1st request expires and then another api request available 
        // exactly like sliding window..
        if (count == 1){
            redisTemplate.expire(key,WINDOW_SECONDS,TimeUnit.SECONDS);
        }


        return count <= MAX_REQUESTS;
    }
}
