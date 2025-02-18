package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.common.util.CodeGenerator;
import com.example.lunchboxkiosk.model.dto.common.CategoryDetailDto;
import com.example.lunchboxkiosk.model.dto.response.GetBrandCategoriesResponseDto;
import com.example.lunchboxkiosk.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "브랜드 별 카테고리 목록 조회")
    @GetMapping()
    public ResponseEntity<GetBrandCategoriesResponseDto> getBrandCategories(@Valid @RequestParam(value = "brand_id") String brandId) {
        CodeGenerator.validateIdFormat("B", brandId);
        List<CategoryDetailDto> categories = categoryService.getCategoriesByBrandId(brandId);

        return ResponseEntity.ok(GetBrandCategoriesResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .categories(categories)
                .build());
    }
}
