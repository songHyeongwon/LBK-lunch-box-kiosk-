package com.example.lunchboxkiosk.model.dto.response;

import com.example.lunchboxkiosk.model.dto.common.MenuDetailDto;
import com.example.lunchboxkiosk.model.dto.common.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class GetOrderResponseDto extends ResponseDto {

    private OrderDto order;
    private List<MenuDetailDto> menus;
}
