package com.example.lunchboxkiosk.model.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String id;
    private List<OrderMenuDto> menus;
    private LocalDateTime orderDate;
    private String customerEmail;
}
