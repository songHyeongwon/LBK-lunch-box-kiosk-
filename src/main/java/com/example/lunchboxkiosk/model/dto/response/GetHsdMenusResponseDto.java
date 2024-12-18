package com.example.lunchboxkiosk.model.dto.response;

import com.example.lunchboxkiosk.model.dto.HsdCategoryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class GetHsdMenusResponseDto extends ResponseDto {

    private List<HsdCategoryDto> hsdMenus;
}
