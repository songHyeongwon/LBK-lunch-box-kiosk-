package com.example.lunchboxkiosk.model.entity.hsd;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdOrder {

    private String orderId;
    private String ip;
    private List<HsdOrderMenu> orderMenus;
}
