package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.model.dto.common.*;
import com.example.lunchboxkiosk.repository.MenuRepository;
import com.example.lunchboxkiosk.repository.MenuSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final BrandService brandService;
    private final CategoryService categoryService;
    private final RedisUtilService redisUtilService;
    private final MenuRepository menuRepository;
    private final MenuSearchRepository menuSearchRepository;

    private MenuDto findMenuById(String menuId) {
        String keyPattern = "menu:*:" + menuId;
        String key = redisUtilService.getKey(keyPattern);
        if (key == null) {
            throw new RuntimeException("menuId: " + menuId + " not found");
        }

        return menuRepository.findById(key);
    }

    public PageDto getPageInfo(String key, int page, int size) {
        Long totalElements = menuSearchRepository.countAllByKey(key);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return PageDto.builder()
                .totalPages(totalPages)
                .currentPage(page)
                .size(size)
                .totalElements(totalElements)
                .build();
    }

    public String getKeyFromPattern(String keyPattern) {
        return redisUtilService.getKey(keyPattern);
    }

    public List<MenuDetailDto> getMenus(String key, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = startIndex + size - 1;

        Set<Object> objects = menuSearchRepository.findAllWithPaging(key, startIndex, endIndex);
        if (objects == null || objects.isEmpty()) {
            throw new RuntimeException("Resource not found.");
        }

        List<MenuDetailDto> menuDetails = new ArrayList<>();
        for (Object menuId : objects) {
            MenuDto menu = findMenuById(menuId.toString());
            if (menu == null) {
                continue;
            }
            CategoryDto category = categoryService.getCategoryById(menu.getCategoryId());
            BrandDto brand = brandService.findBrandById(menu.getBrandId());

            MenuDetailDto detailDto = MenuDetailDto.builder()
                    .menu(menu)
                    .brand(brand)
                    .category(category)
                    .build();
            menuDetails.add(detailDto);
        }

        return menuDetails;
    }

    public List<MenuDetailDto> getMenusByBrandId(String key, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = startIndex + size - 1;

        Set<Object> objects = menuSearchRepository.findByBrandIdWithPaging(key, startIndex, endIndex);
        if (objects == null || objects.isEmpty()) {
            throw new RuntimeException("Resource not found.");
        }

        List<MenuDetailDto> menuDetails = new ArrayList<>();
        for (Object menuId : objects) {
            MenuDto menu = findMenuById(menuId.toString());
            if (menu == null) {
                continue;
            }
            CategoryDto category = categoryService.getCategoryById(menu.getCategoryId());
            BrandDto brand = brandService.findBrandById(menu.getBrandId());

            MenuDetailDto detailDto = MenuDetailDto.builder()
                    .menu(menu)
                    .brand(brand)
                    .category(category)
                    .build();
            menuDetails.add(detailDto);
        }

        return menuDetails;
    }

    public List<MenuDetailDto> getMenusByBrandIdAndCategoryId(String key, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = startIndex + size - 1;

        Set<Object> objects = menuSearchRepository.findByBrandIdAndCategoryIdWithPaging(key, startIndex, endIndex);
        if (objects == null || objects.isEmpty()) {
            throw new RuntimeException("Resource not found.");
        }

        List<MenuDetailDto> menuDetails = new ArrayList<>();
        for (Object menuId : objects) {
            MenuDto menu = findMenuById(menuId.toString());
            if (menu == null) {
                continue;
            }
            CategoryDto category = categoryService.getCategoryById(menu.getCategoryId());
            BrandDto brand = brandService.findBrandById(menu.getBrandId());

            MenuDetailDto detailDto = MenuDetailDto.builder()
                    .menu(menu)
                    .brand(brand)
                    .category(category)
                    .build();
            menuDetails.add(detailDto);
        }

        return menuDetails;
    }

    public MenuDetailDto getMenuByMenuId(String menuId) {
        MenuDto menu = findMenuById(menuId);
        if (menu == null) {
            throw new RuntimeException("Resource not found.");
        }

        CategoryDto category = categoryService.getCategoryById(menu.getCategoryId());
        BrandDto brand = brandService.findBrandById(menu.getBrandId());
        MenuDetailDto menuDetail = MenuDetailDto.builder()
                .menu(menu)
                .brand(brand)
                .category(category)
                .build();

        return menuDetail;
    }
}
