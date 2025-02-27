package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.common.util.CodeGenerator;
import com.example.lunchboxkiosk.model.dto.common.MenuDetailDto;
import com.example.lunchboxkiosk.model.dto.common.PageDto;
import com.example.lunchboxkiosk.model.dto.response.GetBrandCategoryMenusResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetBrandMenusResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetMenuResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetMenusResponseDto;
import com.example.lunchboxkiosk.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "전체 메뉴 목록 조회")
    @GetMapping
    public ResponseEntity<GetMenusResponseDto> getMenus(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "20") int size) {
        String key = "search:brand:all:category:all:menu:all";
        List<MenuDetailDto> menus = menuService.getMenus(key, page, size);
        PageDto pageInfo = menuService.getPageInfo(key, page, size);

        return ResponseEntity.ok(GetMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .menus(menus)
                .page(pageInfo)
                .build());
    }

    @Operation(summary = "브랜드 별 메뉴 목록 조회")
    @GetMapping("/{brand_id}")
    public ResponseEntity<GetBrandMenusResponseDto> getBrandMenus(@PathVariable(name = "brand_id") String brandId,
                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        CodeGenerator.validateIdFormat("B", brandId);
        String key = "search:brand:" + brandId + ":category:all:menu:all";
        List<MenuDetailDto> menus = menuService.getMenusByBrandId(key, page, size);
        PageDto pageInfo = menuService.getPageInfo(key, page, size);

        return ResponseEntity.ok(GetBrandMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .menus(menus)
                .page(pageInfo)
                .build());
    }

    @Operation(summary = "브랜드+카테고리 별 메뉴 목록 조회")
    @GetMapping("/{brand_id}/{category_id}")
    public ResponseEntity<GetBrandCategoryMenusResponseDto> getBrandCategoryMenus(@PathVariable(name = "brand_id") String brandId,
                                                                                  @PathVariable(name = "category_id") String categoryId,
                                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        CodeGenerator.validateIdFormat("B", brandId);
        CodeGenerator.validateIdFormat("C", categoryId);
        String keyPattern = "search:brand:" + brandId + ":category:" + categoryId + ":*";
        String key = menuService.getKeyFromPattern(keyPattern);
        List<MenuDetailDto> menus = menuService.getMenusByBrandIdAndCategoryId(key, page, size);
        PageDto pageInfo = menuService.getPageInfo(key, page, size);

        return ResponseEntity.ok(GetBrandCategoryMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .menus(menus)
                .page(pageInfo)
                .build());
    }

    @Operation(summary = "메뉴 상세 조회")
    @GetMapping("/detail/{menu_id}")
    public ResponseEntity<GetMenuResponseDto> getMenu(@PathVariable(name = "menu_id") String menuId) {
        CodeGenerator.validateIdFormat("M", menuId);
        MenuDetailDto menu = menuService.getMenuDetailById(menuId);

        return ResponseEntity.ok(GetMenuResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .menu(menu)
                .build());
    }
}
