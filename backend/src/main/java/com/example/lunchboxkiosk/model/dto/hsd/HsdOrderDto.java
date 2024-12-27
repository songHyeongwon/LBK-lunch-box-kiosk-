package com.example.lunchboxkiosk.model.dto.hsd;

import com.example.lunchboxkiosk.model.entity.hsd.HsdOrderMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdOrderDto {

    private String orderId;
    private String ip;
    private List<HsdOrderMenu> orderMenus;
}
