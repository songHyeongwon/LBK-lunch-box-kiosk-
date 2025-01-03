package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.model.dto.common.BrandDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<BrandDto> getAllBrands() {
        Set<String> keys = redisTemplate.keys("brands:*"); // 모든 브랜드 키를 가져옴
        if (keys != null && !keys.isEmpty()) {
            return keys.stream()
                    .map(key -> {
                        Object obj = redisTemplate.opsForValue().get(key);
                        if (obj != null) {
                            return objectMapper.convertValue(obj, BrandDto.class); // DTO로 변환
                        }
                        throw new RuntimeException("Redis value is null for key: " + key);
                    })
                    .toList();
        }
        throw new RuntimeException("No brands found in Redis.");
    }
}
