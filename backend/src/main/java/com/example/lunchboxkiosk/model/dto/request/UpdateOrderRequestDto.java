package com.example.lunchboxkiosk.model.dto.request;

import com.example.lunchboxkiosk.model.dto.common.OrderMenuDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequestDto {

    @NotBlank
    private String id;

    @NotBlank
    private String phoneNumber;

    @NotEmpty
    private List<OrderMenuDto> menus;
}
