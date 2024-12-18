package com.example.lunchboxkiosk.model.entity;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdCategory {
    private String name;
    private List<HsdCategory> subCategories;
    private List<HsdMenu> menus;
}
