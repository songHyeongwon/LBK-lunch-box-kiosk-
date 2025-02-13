package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.NotFoundException;
import com.example.lunchboxkiosk.common.util.CodeGenerator;
import com.example.lunchboxkiosk.model.dto.common.MenuDetailDto;
import com.example.lunchboxkiosk.model.dto.common.OrderDetailDto;
import com.example.lunchboxkiosk.model.dto.common.OrderDto;
import com.example.lunchboxkiosk.model.dto.request.CreateOrderRequestDto;
import com.example.lunchboxkiosk.model.dto.request.UpdateOrderRequestDto;
import com.example.lunchboxkiosk.model.dto.response.CreateOrderResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetOrderResponseDto;
import com.example.lunchboxkiosk.model.dto.response.GetOrdersByPhoneNumberResponseDto;
import com.example.lunchboxkiosk.model.dto.response.UpdateOrderResponseDto;
import com.example.lunchboxkiosk.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성")
    @PostMapping()
    public ResponseEntity<CreateOrderResponseDto> createOrder(@Valid @RequestBody CreateOrderRequestDto params) {
        OrderDto orderDto = orderService.createOrder(params);

        return ResponseEntity.ok(CreateOrderResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .order(orderDto)
                .build());
    }
    
    @Operation(summary = "주문 수정: 전체")
    @PutMapping()
    public ResponseEntity<UpdateOrderResponseDto> updateOrder(@Valid @RequestBody UpdateOrderRequestDto params) {
        CodeGenerator.validateIdFormat("O", params.getId());
        OrderDto newOrderDto = orderService.updateOrder(params);

        return ResponseEntity.ok(UpdateOrderResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .order(newOrderDto)
                .build());
    }

    @Operation(summary = "주문 상세 조회: 주문 번호")
    @GetMapping("/detail/{order_id}")
    public ResponseEntity<GetOrderResponseDto> getOrder(@PathVariable("order_id") String orderId) {
        CodeGenerator.validateIdFormat("O", orderId);
        String keyPattern = "*:" + orderId;
        String key = orderService.makeOrderKey(keyPattern);
        OrderDto orderDto = orderService.getOrderByKey(key)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REDIS_KEY_NOT_FOUND, key));

        List<MenuDetailDto> menuDetailDtos = orderService.getMenuDetailByOrderMenu(orderDto);

        return ResponseEntity.ok(GetOrderResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .orderDetail(OrderDetailDto.builder()
                        .order(orderDto)
                        .menus(menuDetailDtos)
                        .build())
                .build());
    }

    @Operation(summary = "사용자 별 주문 목록 조회")
    @GetMapping()
    public ResponseEntity<GetOrdersByPhoneNumberResponseDto> getOrdersByPhoneNumber(@RequestParam("phone_number") String phoneNumber) {
        String keyPattern = "*:" + phoneNumber + ":*";
        Set<String> keys = orderService.makeOrderKeys(keyPattern);
        List<OrderDto> orderDtos = orderService.getOrdersByKey(keys);
        List<OrderDetailDto> orderDetailDtos = orderService.getMenuDetailsByOrderMenu(orderDtos);

        return ResponseEntity.ok(GetOrdersByPhoneNumberResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .orderDetails(orderDetailDtos)
                .build());
    }
}
