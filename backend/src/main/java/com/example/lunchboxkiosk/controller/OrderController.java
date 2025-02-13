package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.common.util.CodeGenerator;
import com.example.lunchboxkiosk.model.dto.common.OrderDto;
import com.example.lunchboxkiosk.model.dto.request.CreateOrderRequestDto;
import com.example.lunchboxkiosk.model.dto.request.UpdateOrderRequestDto;
import com.example.lunchboxkiosk.model.dto.response.CreateOrderResponseDto;
import com.example.lunchboxkiosk.model.dto.response.UpdateOrderResponseDto;
import com.example.lunchboxkiosk.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

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

    /**
     * TODO.
     *  - 주문 삭제
     *  - 사용자 별 주문 내역 조회
     *  - 날짜 별 주문 내역 조회
     *  - 주문 상세 조회
     *  등등
     */
}
