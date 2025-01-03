package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.model.dto.common.*;
import com.example.lunchboxkiosk.repository.MenuRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final BrandService brandService;
    private final MenuRepository menuRepository;

    private String findKeyByPattern(String pattern) {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(100).build());

        while (cursor.hasNext()) {
            return new String(cursor.next());
        }
        return null;
    }

    private MenuDto findMenuById(String menuId) {
        String keyPattern = "menus:*:" + menuId;
        String key = findKeyByPattern(keyPattern);
        if (key == null) {
            throw new IllegalArgumentException("menuId: " + menuId + " not found");
        }
        Object menuData = redisTemplate.opsForValue().get(key);

        return objectMapper.convertValue(menuData, MenuDto.class);
    }

    private CategoryDto findCategoryById(String categoryId) {
        String keyPattern = "categories:*:" + categoryId;
        String key = findKeyByPattern(keyPattern);
        if (key == null) {
            throw new IllegalArgumentException("categoryId: " + categoryId + " not found");
        }
        Object categoryData = redisTemplate.opsForValue().get(key);

        return objectMapper.convertValue(categoryData, CategoryDto.class);
    }

    private BrandDto findBrandById(String brandId) {
        List<BrandDto> brands = brandService.getAllBrands();

        return brands.stream()
                .filter(brand -> brandId.equals(brand.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + brandId + " not found."));
    }

    public List<MenuDetailDto> getAllMenus(int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = startIndex + size - 1;

        Set<Object> allMenuIds = redisTemplate.opsForZSet().range("all_menu_ids", startIndex, endIndex);
        if (allMenuIds == null || allMenuIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<MenuDetailDto> menuDetails = new ArrayList<>();
        for (Object menuId : allMenuIds) {
            MenuDto menu = findMenuById(menuId.toString());
            if (menu == null) {
                continue;
            }

            CategoryDto category = findCategoryById(menu.getCategoryId());
            BrandDto brand = findBrandById(menu.getBrandId());

            MenuDetailDto detailDto = MenuDetailDto.builder()
                    .menu(menu)
                    .brand(brand)
                    .category(category)
                    .build();
            menuDetails.add(detailDto);
        }

        return menuDetails;
    }

    public PageDto getPageInfo(int page, int size) {
        Long totalElements = redisTemplate.opsForZSet().zCard("all_menu_ids");
        int total = (int) Math.ceil((double) totalElements / size);

        return PageDto.builder()
                .total(total)
                .number(page)
                .size(size)
                .totalElements(totalElements)
                .build();
    }
}
