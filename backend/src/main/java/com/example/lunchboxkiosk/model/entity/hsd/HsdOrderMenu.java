package com.example.lunchboxkiosk.model.entity.hsd;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdOrderMenu {

    private HsdMenu menu;
    private int quantity;
}
