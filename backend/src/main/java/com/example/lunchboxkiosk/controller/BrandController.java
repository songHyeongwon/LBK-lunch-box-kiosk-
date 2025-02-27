package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.model.dto.common.BrandDto;
import com.example.lunchboxkiosk.model.dto.response.GetBrandsResponseDto;
import com.example.lunchboxkiosk.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @Operation(summary = "전체 브랜드 목록 조회")
    @GetMapping()
    public ResponseEntity<GetBrandsResponseDto> getBrands() {
        List<BrandDto> brands = brandService.getAllBrands();

        return ResponseEntity.ok(GetBrandsResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .brands(brands)
                .build());
    }
}
