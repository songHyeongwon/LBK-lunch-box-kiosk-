package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.model.dto.common.BrandDto;
import com.example.lunchboxkiosk.model.dto.common.CategoryDto;
import com.example.lunchboxkiosk.model.dto.common.MenuDto;
import com.example.lunchboxkiosk.model.entity.Brand;
import com.example.lunchboxkiosk.model.entity.Category;
import com.example.lunchboxkiosk.model.entity.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawledDataService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ModelMapper modelMapper;

    public void saveBrands(BrandDto brandDto) {
        Brand brand = modelMapper.map(brandDto, Brand.class);
        String key = "brand:" + brand.getId();
        redisTemplate.opsForValue().set(key, brand);
    }

    public void saveCategories(String brandId, List<CategoryDto> categoryDtoList) {
        categoryDtoList.forEach(categoryDto -> {
            Category category = modelMapper.map(categoryDto, Category.class);
            String key = "category:" + brandId + ":" + category.getId();
            redisTemplate.opsForValue().set(key, category);
        });
    }

    public void saveMenus(String brandId, List<MenuDto> menuDtoList) {
        menuDtoList.forEach(menuDto -> {
            Menu menu = modelMapper.map(menuDto, Menu.class);
            String key = "menu:" + brandId + ":" + menu.getCategoryId() + ":" + menu.getId();
            redisTemplate.opsForValue().set(key, menu);

            redisTemplate.opsForZSet().add("search:brand:all:category:all:menu:all", menu.getId(), System.currentTimeMillis());
            redisTemplate.opsForZSet().add("search:brand:" + brandId + ":category:all:menu:all", menu.getId(), System.currentTimeMillis());
            redisTemplate.opsForZSet().add("search:brand:" + brandId + ":category:" + menu.getCategoryId() + ":menu:all", menu.getId(), System.currentTimeMillis());
        });
    }
}
