package com.example.lunchboxkiosk.model.dto.hsd;

import com.example.lunchboxkiosk.model.entity.hsd.HsdMenu;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HsdCategoryDto {

    private String name;
    private List<HsdCategoryDto> subCategories;
    private List<HsdMenu> menus;
}
