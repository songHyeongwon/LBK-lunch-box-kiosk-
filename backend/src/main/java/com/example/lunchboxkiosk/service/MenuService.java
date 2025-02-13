package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.NotFoundException;
import com.example.lunchboxkiosk.model.dto.common.*;
import com.example.lunchboxkiosk.repository.MenuRepository;
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

    public MenuDto getMenuById(String menuId) {
        String keyPattern = "menu:*:" + menuId;
        String key = redisUtilService.getKey(keyPattern);
        if (key == null) {
            throw new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, "null");
        }

        return menuRepository.findByKey(key);
    }

    public PageDto getPageInfo(String key, int page, int size) {
        Long totalElements = menuRepository.countAllByKey(key);
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

        Set<Object> objects = menuRepository.findAllWithPaging(key, startIndex, endIndex);
        if (objects == null || objects.isEmpty()) {
            throw new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, key);
        }

        List<MenuDetailDto> menuDetailDtos = new ArrayList<>();
        for (Object menuId : objects) {
            MenuDetailDto menuDetailDto = getMenuDetailById(menuId.toString());
            menuDetailDtos.add(menuDetailDto);
        }

        return menuDetailDtos;
    }

    public List<MenuDetailDto> getMenusByBrandId(String key, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = startIndex + size - 1;

        Set<Object> objects = menuRepository.findByBrandIdWithPaging(key, startIndex, endIndex);
        if (objects == null || objects.isEmpty()) {
            throw new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, key);
        }

        List<MenuDetailDto> menuDetailDtos = new ArrayList<>();
        for (Object menuId : objects) {
            MenuDetailDto menuDetailDto = getMenuDetailById(menuId.toString());
            menuDetailDtos.add(menuDetailDto);
        }

        return menuDetailDtos;
    }

    public List<MenuDetailDto> getMenusByBrandIdAndCategoryId(String key, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = startIndex + size - 1;

        Set<Object> objects = menuRepository.findByBrandIdAndCategoryIdWithPaging(key, startIndex, endIndex);
        if (objects == null || objects.isEmpty()) {
            throw new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, key);
        }

        List<MenuDetailDto> menuDetailDtos = new ArrayList<>();
        for (Object menuId : objects) {
            MenuDetailDto menuDetailDto = getMenuDetailById(menuId.toString());
            menuDetailDtos.add(menuDetailDto);
        }

        return menuDetailDtos;
    }

    public MenuDetailDto getMenuDetailById(String menuId) {
        MenuDto menu = getMenuById(menuId);
        if (menu == null) {
            throw new NotFoundException(ErrorCode.MENU_NOT_FOUND, menuId);
        }

        CategoryDto category = categoryService.getCategoryById(menu.getCategoryId());
        BrandDto brand = brandService.findBrandById(menu.getBrandId());

        return MenuDetailDto.builder()
                .menu(menu)
                .brand(brand)
                .category(category)
                .build();
    }
}
