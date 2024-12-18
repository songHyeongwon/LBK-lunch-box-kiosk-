package com.example.lunchboxkiosk.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdCategory implements Serializable {
    private String name;
    private List<HsdCategory> subCategories;
    private List<HsdMenu> menus;
}
