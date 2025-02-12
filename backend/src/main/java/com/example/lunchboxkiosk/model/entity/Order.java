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
    private User user;
    private List<OrderMenu> menus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
