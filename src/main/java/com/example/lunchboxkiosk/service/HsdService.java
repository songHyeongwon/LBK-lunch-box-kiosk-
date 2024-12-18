package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.model.dto.HsdCategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HsdService {

    private RedisTemplate<String, Object> redisTemplate;
    private static final String HSD_REDIS_KEY = "hsd_categories";

    // 객체 저장
    public void saveCategories(List<HsdCategoryDto> categories) {
        redisTemplate.opsForValue().set(HSD_REDIS_KEY, categories);
    }
}
