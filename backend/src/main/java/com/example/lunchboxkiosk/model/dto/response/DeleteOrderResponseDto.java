package com.example.lunchboxkiosk.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=true)
public class DeleteOrderResponseDto extends ResponseDto {
}
