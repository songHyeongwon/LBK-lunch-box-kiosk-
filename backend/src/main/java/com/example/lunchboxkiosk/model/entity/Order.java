package com.example.lunchboxkiosk.model.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String id;
    private String phoneNumber;
    private List<OrderMenu> menus;
    private int totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
