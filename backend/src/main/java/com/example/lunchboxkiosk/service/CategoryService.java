package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.NotFoundException;
import com.example.lunchboxkiosk.model.dto.common.CategoryDetailDto;
import com.example.lunchboxkiosk.model.dto.common.CategoryDto;
import com.example.lunchboxkiosk.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final RedisUtilService redisUtilService;
    private final CategoryRepository categoryRepository;

    public CategoryDto getCategoryById(String categoryId) {
        String keyPattern = "category:*:" + categoryId;
        String key = redisUtilService.getKey(keyPattern);
        if (key == null) {
            throw new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, "null");
        }

        return categoryRepository.findCategoryById(key);
    }

    private String extractCategoryIdFromKey(String key) {
        Pattern pattern = Pattern.compile("C-\\S+");
        Matcher matcher = pattern.matcher(key);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public List<CategoryDetailDto> getCategoriesByBrandId(String brandId) {
        String keyPattern = "category:" + brandId + ":*";
        Set<String> keys = redisUtilService.getKeys(keyPattern);

        List<CategoryDto> categories = new ArrayList<>();
        for (String key : keys) {
            String categoryId = extractCategoryIdFromKey(key);
            if (categoryId == null) {
                throw new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND, "null");
            }

            CategoryDto category = getCategoryById(categoryId);
            categories.add(category);
        }

        Map<String, CategoryDetailDto> categoryMap = new HashMap<>();
        List<CategoryDetailDto> rootCategories = new ArrayList<>();
        for (CategoryDto category : categories) {
            CategoryDetailDto categoryDetail = categoryMap.computeIfAbsent(category.getId(), k ->
                    CategoryDetailDto.builder()
                            .id(category.getId())
                            .childs(new ArrayList<>())
                            .build()
            );

            if (categoryDetail.getName() == null) {
                categoryDetail.setName(category.getName());
                categoryDetail.setParentId(category.getParentId());
            }

            if (categoryDetail.getParentId() == null) {
                rootCategories.add(categoryDetail);
            } else {
                CategoryDetailDto parent = categoryMap.computeIfAbsent(category.getParentId(), k ->
                        CategoryDetailDto.builder()
                                .id(category.getParentId())
                                .childs(new ArrayList<>()) // 기존 자식 정보 유지
                                .build()
                );

                if (!parent.getChilds().contains(categoryDetail)) {
                    parent.getChilds().add(categoryDetail);
                }
            }
        }

        return rootCategories;
    }
}
