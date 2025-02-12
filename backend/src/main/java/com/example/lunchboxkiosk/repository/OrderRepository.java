package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.model.dto.common.BrandDto;
import com.example.lunchboxkiosk.model.dto.common.OrderDto;
import com.example.lunchboxkiosk.model.entity.Brand;
import com.example.lunchboxkiosk.model.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public OrderDto save(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String key = order.getCreatedAt().format(formatter) + ":" + order.getPhoneNumber() + ":" + order.getId();
        redisTemplate.opsForValue().set(key, order, 7, TimeUnit.DAYS);

        return modelMapper.map(order, OrderDto.class);
    }

    public void saveBrands(BrandDto brandDto) {
        Brand brand = modelMapper.map(brandDto, Brand.class);
        String key = "brand:" + brand.getId();
        redisTemplate.opsForValue().set(key, brand, 1, TimeUnit.DAYS);
    }
}
