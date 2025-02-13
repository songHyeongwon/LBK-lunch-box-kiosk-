package com.example.lunchboxkiosk.model.dto.response;

import com.example.lunchboxkiosk.model.dto.common.MenuDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class GetMenuResponseDto extends ResponseDto {

    private MenuDetailDto menu;
}
