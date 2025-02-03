package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.model.dto.response.GetBrandCategoryMenusResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetBrandMenusResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetMenuResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetMenusResponseDto;
import com.example.lunchboxkiosk.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 전체 메뉴 목록 조회
    @GetMapping
    public ResponseEntity<GetMenusResponseDto> getMenus(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "20") int size) {
        GetMenusResponseDto response = menuService.getMenus(page, size);
        return ResponseEntity.ok(response);
    }

    // 브랜드 메뉴 목록 조회
    @GetMapping("/{brandId}")
    public ResponseEntity<GetBrandMenusResponseDto> getBrandMenus(@PathVariable(name = "brandId") String brandId,
                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        GetBrandMenusResponseDto response = menuService.getMenusByBrandId(brandId, page, size);
        return ResponseEntity.ok(response);
    }
    
    // 브랜드+카테고리 메뉴 목록 조회
    @GetMapping("/{brandId}/{categoryId}")
    public ResponseEntity<GetBrandCategoryMenusResponseDto> getBrandCategoryMenus(@PathVariable(name = "brandId") String brandId,
                                                                                  @PathVariable(name = "categoryId") String categoryId,
                                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        GetBrandCategoryMenusResponseDto response = menuService.getMenusByBrandIdAndCategoryId(brandId, categoryId, page, size);
        return ResponseEntity.ok(response);
    }


    // 메뉴 조회
    @GetMapping("/detail/{menuId}")
    public ResponseEntity<GetMenuResponseDto> getMenu(@PathVariable(name = "menuId") String menuId) {

        GetMenuResponseDto response = menuService.getMenuByMenuId(menuId);
        return ResponseEntity.ok(response);
    }
}
