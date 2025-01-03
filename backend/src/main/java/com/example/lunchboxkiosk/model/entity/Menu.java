package com.example.lunchboxkiosk.model.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {

    private String id;
    private String name;
    private int price;
    private String imageUrl;
    private String brandId;
    private String categoryId;
}
