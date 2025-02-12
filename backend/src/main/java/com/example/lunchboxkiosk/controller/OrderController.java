package com.example.lunchboxkiosk.controller;

import com.example.lunchboxkiosk.model.dto.request.CreateOrderRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequestDto params) {

        log.info("params: {}", params);
        return null;
    }
}
