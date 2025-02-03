package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.model.dto.common.MenuDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MenuRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public MenuDto findById(String key) {
        Object menuData = redisTemplate.opsForValue().get(key);
        return objectMapper.convertValue(menuData, MenuDto.class);
    }
}
