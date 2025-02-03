package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.model.dto.common.CategoryDto;
import com.example.lunchboxkiosk.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final RedisUtilService redisUtilService;
    private final CategoryRepository categoryRepository;

    public CategoryDto findCategoryById(String categoryId) {
        String keyPattern = "category:*:" + categoryId;
        String key = redisUtilService.getKey(keyPattern);
        if (key == null) {
            throw new IllegalArgumentException("categoryId: " + categoryId + " not found");
        }

        return categoryRepository.findCategoryById(key);
    }
}
