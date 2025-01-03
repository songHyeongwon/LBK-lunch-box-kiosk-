package com.example.lunchboxkiosk.model.dto.response;

import com.example.lunchboxkiosk.model.dto.common.BrandDto;
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
public class GetBrandsResponseDto extends ResponseDto {

    private List<BrandDto> brands;
}
