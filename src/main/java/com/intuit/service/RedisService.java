package com.intuit.service;


import com.intuit.models.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.intuit.utils.Constants.CAR_KEY_PREFIX;

@Service
public class RedisService {

    @Value("${car.prefix}")
    private String CAR_KEY_PREFIX;

    private final RedisTemplate<String, Car> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Car> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void getCachedCar(String id) {
        String cacheKey = CAR_KEY_PREFIX + id;
        redisTemplate.opsForValue().get(cacheKey);
    }

    public void putCarToCache(String id, Car car) {
        String cacheKey = CAR_KEY_PREFIX + id;
        redisTemplate.opsForValue().set(cacheKey, car);
    }
}
