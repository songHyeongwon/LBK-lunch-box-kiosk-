package com.example.lunckboxkiosk.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HsdMenuDto {

    private String name;
    private int price;
    private String imageUrl;
}
