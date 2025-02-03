package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.model.dto.common.MenuDetailDto;
import com.example.lunchboxkiosk.model.dto.common.PageDto;
import com.example.lunchboxkiosk.model.dto.response.GetBrandCategoryMenusResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetBrandMenusResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetMenuResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetMenusResponseDto;
import com.example.lunchboxkiosk.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        String key = "search:brand:all:category:all:menu:all";
        List<MenuDetailDto> menus = menuService.getMenus(key, page, size);
        PageDto pageInfo = menuService.getPageInfo(key, page, size);

        return ResponseEntity.ok(GetMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .menus(menus)
                .page(pageInfo)
                .build());
    }

    // 브랜드 메뉴 목록 조회
    @GetMapping("/{brandId}")
    public ResponseEntity<GetBrandMenusResponseDto> getBrandMenus(@PathVariable(name = "brandId") String brandId,
                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        String key = "search:brand:" + brandId + ":category:all:menu:all";
        List<MenuDetailDto> menus = menuService.getMenusByBrandId(key, page, size);
        PageDto pageInfo = menuService.getPageInfo(key, page, size);

        return ResponseEntity.ok(GetBrandMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .menus(menus)
                .page(pageInfo)
                .build());
    }
    
    // 브랜드+카테고리 메뉴 목록 조회
    @GetMapping("/{brandId}/{categoryId}")
    public ResponseEntity<GetBrandCategoryMenusResponseDto> getBrandCategoryMenus(@PathVariable(name = "brandId") String brandId,
                                                                                  @PathVariable(name = "categoryId") String categoryId,
                                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        String keyPattern = "search:brand:" + brandId + ":category:" + categoryId + ":*";
        String key = menuService.getKeyFromPattern(keyPattern);
        List<MenuDetailDto> menus = menuService.getMenusByBrandIdAndCategoryId(key, page, size);
        PageDto pageInfo = menuService.getPageInfo(key, page, size);

        return ResponseEntity.ok(GetBrandCategoryMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .menus(menus)
                .page(pageInfo)
                .build());
    }


    // 메뉴 조회
    @GetMapping("/detail/{menuId}")
    public ResponseEntity<GetMenuResponseDto> getMenu(@PathVariable(name = "menuId") String menuId) {
        MenuDetailDto menu = menuService.getMenuByMenuId(menuId);

        return ResponseEntity.ok(GetMenuResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .menu(menu)
                .build());
    }
}
