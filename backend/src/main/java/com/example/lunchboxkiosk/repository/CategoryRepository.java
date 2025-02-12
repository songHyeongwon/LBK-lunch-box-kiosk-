package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.model.dto.common.CategoryDto;
import com.example.lunchboxkiosk.model.entity.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public void saveCategories(String brandId, List<CategoryDto> categoryDtoList) {
        categoryDtoList.forEach(categoryDto -> {
            Category category = modelMapper.map(categoryDto, Category.class);
            String key = "category:" + brandId + ":" + category.getId();
            redisTemplate.opsForValue().set(key, category, 1, TimeUnit.DAYS);
        });
    }

    public CategoryDto findCategoryById(String key) {
        Object categoryData = redisTemplate.opsForValue().get(key);

        return objectMapper.convertValue(categoryData, CategoryDto.class);
    }
}
