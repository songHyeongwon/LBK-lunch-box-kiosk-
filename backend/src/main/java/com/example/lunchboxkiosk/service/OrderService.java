package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.NotFoundException;
import com.example.lunchboxkiosk.common.util.CodeGenerator;
import com.example.lunchboxkiosk.model.dto.common.*;
import com.example.lunchboxkiosk.model.dto.request.CreateOrderRequestDto;
import com.example.lunchboxkiosk.model.dto.request.DeleteOrderRequestDto;
import com.example.lunchboxkiosk.model.dto.request.UpdateOrderRequestDto;
import com.example.lunchboxkiosk.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MenuService menuService;
    private final RedisUtilService redisUtilService;
    private final OrderRepository orderRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private int calculateTotalPrice(List<OrderMenuDto> menus) {
        return menus.stream()
                .mapToInt(orderMenu -> {
                    MenuDto menuDto = menuService.getMenuById(orderMenu.getId());
                    return menuDto.getPrice() * orderMenu.getQuantity();
                })
                .sum();
    }

    public OrderDto createOrder(CreateOrderRequestDto params) {
        String orderId = CodeGenerator.generateId("O");
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .phoneNumber(params.getPhoneNumber())
                .menus(params.getMenus())
                .totalPrice(calculateTotalPrice(params.getMenus()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return orderRepository.save(orderDto);
    }

    public Optional<OrderDto> getOrderByKey(String key) {
        return orderRepository.findByKey(key);
    }

    public List<OrderDto> getOrdersByKey(Set<String> keys) {
        List<OrderDto> orderDtos = new ArrayList<>();
        for (String key : keys) {
            orderRepository.findByKey(key).ifPresent(orderDtos::add);
        }
        return orderDtos;
    }

    public OrderDto updateOrder(UpdateOrderRequestDto params) {
        String keyPattern = "*:" + params.getPhoneNumber() + ":" + params.getId();
        String key = makeOrderKey(keyPattern);

        OrderDto orderDto = getOrderByKey(key)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, key));
        orderDto.setMenus(params.getMenus());
        orderDto.setTotalPrice(calculateTotalPrice(params.getMenus()));
        orderDto.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(orderDto);
    }

    public String makeOrderKey(String keyPattern) {
        String key = redisUtilService.getKey(keyPattern);
        if (key == null) {
            throw new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, "null");
        }

        return key;
    }

    public Set<String> makeOrderKeys(String keyPattern) {
        Set<String> keys = redisUtilService.getKeys(keyPattern);
        if (keys == null || keys.isEmpty()) {
            throw new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, "No keys found in Redis.");
        }

        return keys;
    }

    public List<MenuDetailDto> getMenuDetailByOrderMenu(OrderDto orderDto) {
        return orderDto.getMenus().stream()
                .map(orderMenu -> menuService.getMenuDetailById(orderMenu.getId()))
                .collect(Collectors.toList());
    }

    public List<OrderDetailDto> getMenuDetailsByOrderMenu(List<OrderDto> orderDtos) {
        return orderDtos.stream()
                .map(orderDto -> OrderDetailDto.builder()
                        .order(orderDto)
                        .menus(getMenuDetailByOrderMenu(orderDto))
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteOrder(DeleteOrderRequestDto params) {
        String keyPattern = "*:" + params.getPhoneNumber() + ":" + params.getId();
        String key = makeOrderKey(keyPattern);
        orderRepository.deleteOrderByKey(key);
    }
}
