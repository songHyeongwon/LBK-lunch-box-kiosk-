package com.example.lunchboxkiosk.model.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private int status;
    private String message;
}
