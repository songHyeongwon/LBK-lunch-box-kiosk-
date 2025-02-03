package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.model.dto.common.*;
import com.example.lunchboxkiosk.model.dto.response.GetBrandCategoryMenusResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetBrandMenusResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetMenuResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetMenusResponseDto;
import com.example.lunchboxkiosk.repository.MenuRepository;
import com.example.lunchboxkiosk.repository.MenuSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final RedisTemplate<String, Object> redisTemplate;
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

    public GetMenusResponseDto getMenus(int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = startIndex + size - 1;
        String key = "search:brand:all:category:all:menu:all";

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
            CategoryDto category = categoryService.findCategoryById(menu.getCategoryId());
            BrandDto brand = brandService.findBrandById(menu.getBrandId());

            MenuDetailDto detailDto = MenuDetailDto.builder()
                    .menu(menu)
                    .brand(brand)
                    .category(category)
                    .build();
            menuDetails.add(detailDto);
        }

        Long totalElements = menuSearchRepository.countAllByKey(key);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return GetMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .menus(menuDetails)
                .page(PageDto.builder()
                        .totalPages(totalPages)
                        .currentPage(page)
                        .size(size)
                        .totalElements(totalElements)
                        .build())
                .build();
    }

    public GetBrandMenusResponseDto getMenusByBrandId(String brandId, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = startIndex + size - 1;
        String key = "search:brand:" + brandId + ":category:all:menu:all";

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
            CategoryDto category = categoryService.findCategoryById(menu.getCategoryId());
            BrandDto brand = brandService.findBrandById(menu.getBrandId());

            MenuDetailDto detailDto = MenuDetailDto.builder()
                    .menu(menu)
                    .brand(brand)
                    .category(category)
                    .build();
            menuDetails.add(detailDto);
        }

        Long totalElements = menuSearchRepository.countAllByKey(key);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return GetBrandMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .menus(menuDetails)
                .page(PageDto.builder()
                        .totalPages(totalPages)
                        .currentPage(page)
                        .size(size)
                        .totalElements(totalElements)
                        .build())
                .build();
    }

    public GetBrandCategoryMenusResponseDto getMenusByBrandIdAndCategoryId(String brandId, String categoryId, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = startIndex + size - 1;
        String keyPattern = "search:brand:" + brandId + ":category:" + categoryId + ":*";
        String key = redisUtilService.getKey(keyPattern);

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
            CategoryDto category = categoryService.findCategoryById(menu.getCategoryId());
            BrandDto brand = brandService.findBrandById(menu.getBrandId());

            MenuDetailDto detailDto = MenuDetailDto.builder()
                    .menu(menu)
                    .brand(brand)
                    .category(category)
                    .build();
            menuDetails.add(detailDto);
        }

        Long totalElements = menuSearchRepository.countAllByKey(key);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return GetBrandCategoryMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .menus(menuDetails)
                .page(PageDto.builder()
                        .totalPages(totalPages)
                        .currentPage(page)
                        .size(size)
                        .totalElements(totalElements)
                        .build())
                .build();
    }

    public GetMenuResponseDto getMenuByMenuId(String menuId) {
        MenuDto menu = findMenuById(menuId);
        if (menu == null) {
            throw new RuntimeException("Resource not found.");
        }

        CategoryDto category = categoryService.findCategoryById(menu.getCategoryId());
        BrandDto brand = brandService.findBrandById(menu.getBrandId());
        MenuDetailDto detailDto = MenuDetailDto.builder()
                .menu(menu)
                .brand(brand)
                .category(category)
                .build();

        return GetMenuResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .menu(detailDto)
                .build();
    }
}
