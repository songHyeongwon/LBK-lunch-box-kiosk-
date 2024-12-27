package com.example.lunchboxkiosk.model.dto.hsd;

import com.example.lunchboxkiosk.model.entity.hsd.HsdMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdOrderMenuDto {

    private HsdMenu menu;
    private int quantity;
}
