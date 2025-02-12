package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.NotFoundException;
import com.example.lunchboxkiosk.model.dto.common.BrandDto;
import com.example.lunchboxkiosk.model.entity.Brand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BrandRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public void saveBrands(BrandDto brandDto) {
        Brand brand = modelMapper.map(brandDto, Brand.class);
        String key = "brand:" + brand.getId();
        redisTemplate.opsForValue().set(key, brand, 1, TimeUnit.DAYS);
    }

    public List<Brand> findAll() {
        Set<String> keys = redisTemplate.keys("brand:*"); // 모든 브랜드 키를 가져옴
        if (keys == null || keys.isEmpty()) {
            throw new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, "No brand keys found in Redis.");
        }

        return keys.stream()
                .map(key -> Optional.ofNullable(redisTemplate.opsForValue().get(key))
                        .map(obj -> objectMapper.convertValue(obj, Brand.class))
                        .orElseThrow(() -> new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, key))
                )
                .toList();
    }
}
