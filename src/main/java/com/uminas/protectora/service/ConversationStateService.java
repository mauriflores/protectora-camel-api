package com.uminas.protectora.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConversationStateService {

    private final StringRedisTemplate redisTemplate;

    public ConversationStateService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveState(String phone, String state) {
        redisTemplate.opsForValue().set(phone, state);
    }

    public String getState(String phone) {
        return redisTemplate.opsForValue().get(phone);
    }

    public void deleteState(String phone) {
        redisTemplate.delete(phone);
    }

    public void saveDescription(String phone, String description) {
        redisTemplate.opsForValue().set(phone + ":description", description);
    }

    public String getDescription(String phone) {
        return redisTemplate.opsForValue().get(phone + ":description");
    }

    public void saveLocation(String phone, String location) {
        redisTemplate.opsForValue().set(phone + ":location", location);
    }

    public String getLocation(String phone) {
        return redisTemplate.opsForValue().get(phone + ":location");
    }

    public void clearConversation(String phone) {
        redisTemplate.delete(phone);
        redisTemplate.delete(phone + ":description");
        redisTemplate.delete(phone + ":location");
    }
}