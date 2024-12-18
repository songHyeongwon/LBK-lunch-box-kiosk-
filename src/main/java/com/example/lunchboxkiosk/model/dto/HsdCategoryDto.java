package com.example.lunchboxkiosk.model.dto;

import com.example.lunchboxkiosk.model.entity.HsdMenu;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdCategoryDto {

    private String name;
    private List<HsdCategoryDto> subCategories;
    private List<HsdMenu> menus;
}
