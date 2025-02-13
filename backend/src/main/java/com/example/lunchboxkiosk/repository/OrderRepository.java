package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.model.dto.common.OrderDto;
import com.example.lunchboxkiosk.model.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public OrderDto save(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        String key = order.getCreatedAt().format(DATE_FORMATTER) + ":" + order.getPhoneNumber() + ":" + order.getId();
        redisTemplate.opsForValue().set(key, order, 7, TimeUnit.DAYS);

        return modelMapper.map(order, OrderDto.class);
    }

    public Optional<OrderDto> findByKey(String key) {
        Object order = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(objectMapper.convertValue(order, OrderDto.class));
    }
}
