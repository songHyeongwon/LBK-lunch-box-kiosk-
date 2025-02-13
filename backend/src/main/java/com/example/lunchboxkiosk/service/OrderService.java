package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.InvalidValueException;
import com.example.lunchboxkiosk.common.exception.NotFoundException;
import com.example.lunchboxkiosk.common.util.CodeGenerator;
import com.example.lunchboxkiosk.model.dto.common.MenuDto;
import com.example.lunchboxkiosk.model.dto.common.OrderDto;
import com.example.lunchboxkiosk.model.dto.common.OrderMenuDto;
import com.example.lunchboxkiosk.model.dto.request.CreateOrderRequestDto;
import com.example.lunchboxkiosk.model.dto.request.UpdateOrderRequestDto;
import com.example.lunchboxkiosk.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MenuService menuService;
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

    public OrderDto updateOrder(UpdateOrderRequestDto params) {
        String key = params.getCreatedAt().format(DATE_FORMATTER) + ":" + params.getPhoneNumber() + ":" + params.getId();

        OrderDto orderDto = getOrderByKey(key)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, key));
        orderDto.setMenus(params.getMenus());
        orderDto.setTotalPrice(calculateTotalPrice(params.getMenus()));
        orderDto.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(orderDto);
    }
}
