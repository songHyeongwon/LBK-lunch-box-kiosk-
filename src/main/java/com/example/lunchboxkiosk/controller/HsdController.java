package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.model.dto.HsdCategoryDto;
import com.example.lunchboxkiosk.model.dto.response.GetHsdMenusResponseDto;
import com.example.lunchboxkiosk.service.HsdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hsd")
@RequiredArgsConstructor
public class HsdController {

    private final HsdService hsdService;

    @GetMapping("/menus")
    public ResponseEntity<GetHsdMenusResponseDto> getHsdMenus() {
        List<HsdCategoryDto> categories = hsdService.getCategories();

        return ResponseEntity.ok(GetHsdMenusResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .hsdMenus(categories)
                .build());
    }
}
