package com.example.lunchboxkiosk.model.entity;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenu {

    private String id;
    private int quantity;
}
