package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.common.util.CodeGenerator;
import com.example.lunchboxkiosk.model.dto.common.MenuDto;
import com.example.lunchboxkiosk.model.dto.common.OrderDto;
import com.example.lunchboxkiosk.model.dto.common.OrderMenuDto;
import com.example.lunchboxkiosk.model.dto.request.CreateOrderRequestDto;
import com.example.lunchboxkiosk.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MenuService menuService;
    private final OrderRepository orderRepository;

    private int calculateTotalPrice(List<OrderMenuDto> menus) {
        return menus.stream()
                .mapToInt(orderMenu -> {
                    MenuDto menuDto = menuService.getMenuById(orderMenu.getId());
                    return menuDto.getPrice() * orderMenu.getQuantity();
                })
                .sum();
    }

    public OrderDto createOrder(CreateOrderRequestDto params) {
        String orderId = CodeGenerator.generateID("O");
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
}
