package com.example.lunchboxkiosk.model.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawledMenuDataDto {

    private BrandDto brand;
    private List<CategoryDto> categories;
    private List<MenuDto> menus;
}
