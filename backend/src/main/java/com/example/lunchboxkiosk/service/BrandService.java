package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.NotFoundException;
import com.example.lunchboxkiosk.model.dto.common.BrandDto;
import com.example.lunchboxkiosk.repository.BrandRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final ObjectMapper objectMapper;

    public List<BrandDto> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brand -> objectMapper.convertValue(brand, BrandDto.class))
                .toList();
    }

    public BrandDto findBrandById(String brandId) {
        List<BrandDto> brands = getAllBrands();

        return brands.stream()
                .filter(brand -> brandId.equals(brand.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND, brandId));
    }
}
