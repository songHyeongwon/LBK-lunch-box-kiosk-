package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.model.dto.common.BrandDto;
import com.example.lunchboxkiosk.model.entity.Brand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BrandRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<Brand> findAll() {
        Set<String> keys = redisTemplate.keys("brand:*"); // 모든 브랜드 키를 가져옴
        if (keys != null && !keys.isEmpty()) {
            return keys.stream()
                    .map(key -> {
                        Object obj = redisTemplate.opsForValue().get(key);
                        if (obj != null) {
                            return objectMapper.convertValue(obj, Brand.class); // DTO로 변환
                        }
                        throw new RuntimeException("Redis value is null for key: " + key);
                    })
                    .toList();
        }
        throw new RuntimeException("No brand:* found in Redis.");
    }
}
