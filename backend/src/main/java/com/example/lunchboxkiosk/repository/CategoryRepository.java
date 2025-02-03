package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.model.dto.common.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public CategoryDto findCategoryById(String key) {
        Object categoryData = redisTemplate.opsForValue().get(key);

        return objectMapper.convertValue(categoryData, CategoryDto.class);
    }
}
