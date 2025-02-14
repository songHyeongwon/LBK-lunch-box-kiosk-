package com.example.lunchboxkiosk.model.dto.request;

import com.example.lunchboxkiosk.model.dto.common.OrderMenuDto;
import jakarta.validation.constraints.Email;
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
public class CreateOrderRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotEmpty
    private List<OrderMenuDto> menus;
}
