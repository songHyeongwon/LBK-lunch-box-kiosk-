package com.example.lunckboxkiosk.model.dto;

import com.example.lunckboxkiosk.model.entity.HsdMenu;
import com.fasterxml.jackson.annotation.JsonProperty;
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
