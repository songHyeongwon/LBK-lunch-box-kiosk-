package com.example.lunchboxkiosk.model.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdMenu implements Serializable {
    private String name;
    private int price;
    private String imageUrl;
}
