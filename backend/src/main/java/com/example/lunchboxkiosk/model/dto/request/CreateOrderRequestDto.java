package com.example.lunchboxkiosk.model.dto.request;

import com.example.lunchboxkiosk.model.dto.common.OrderMenuDto;
import com.example.lunchboxkiosk.model.dto.common.UserDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
    @NotNull
    private UserDto user;
    @NotEmpty
    private List<OrderMenuDto> menus;
}
