package com.example.lunchboxkiosk.model.entity;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdMenu {
    private String name;
    private int price;
    private String imageUrl;
}
