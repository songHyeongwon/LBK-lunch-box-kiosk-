package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.model.dto.common.MenuDetailDto;
import com.example.lunchboxkiosk.model.dto.common.PageDto;
import com.example.lunchboxkiosk.model.dto.response.GetAllMenusResponseDto;
import com.example.lunchboxkiosk.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;


    @GetMapping
    public ResponseEntity<GetAllMenusResponseDto> getAllMenus(@RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "size", defaultValue = "20") int size) {
        List<MenuDetailDto> menus = menuService.getAllMenus(page, size);
        PageDto pageInfo = menuService.getPageInfo(page, size);

        GetAllMenusResponseDto response = GetAllMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .menus(menus)
                .page(pageInfo)
                .build();
        return ResponseEntity.ok(response);
    }
}
