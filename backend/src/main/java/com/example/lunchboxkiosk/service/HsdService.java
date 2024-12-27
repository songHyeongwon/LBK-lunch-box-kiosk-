package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.model.dto.hsd.HsdCategoryDto;
import com.example.lunchboxkiosk.model.entity.hsd.HsdCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HsdService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    private static final String HSD_REDIS_KEY = "hsd_categories";

    public void saveCategories(List<HsdCategoryDto> categoryDtos) {
        List<HsdCategory> categories = categoryDtos.stream()
                .map(dto -> modelMapper.map(dto, HsdCategory.class)) // ModelMapper를 사용한 변환
                .toList();

        redisTemplate.opsForValue().set(HSD_REDIS_KEY, categories);
    }

    public List<HsdCategoryDto> getCategories() {
        Object obj = redisTemplate.opsForValue().get(HSD_REDIS_KEY);

        if (obj instanceof List<?> list) {
            return list.stream()
                    .map(item -> objectMapper.convertValue(item, HsdCategoryDto.class)) // DTO로 변환
                    .toList();
        }
        throw new RuntimeException("Redis type error: " + (obj != null ? obj.getClass().getName() : "null"));
    }
}
