package com.example.lunchboxkiosk.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdMenuDto {

    private String name;
    private int price;
    private String imageUrl;
}