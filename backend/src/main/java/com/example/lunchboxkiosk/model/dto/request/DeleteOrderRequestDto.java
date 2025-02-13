package com.example.lunchboxkiosk.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteOrderRequestDto {

    @NotBlank
    private String id;

    @NotBlank
    private String phoneNumber;
}
