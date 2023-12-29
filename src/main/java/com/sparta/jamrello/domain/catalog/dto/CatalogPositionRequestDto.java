package com.sparta.jamrello.domain.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CatalogPositionRequestDto(
        @NotBlank(message = "포지션 값을 입력 해주세요.")
        @Size(min = 1, message = "1 이상의 숫자만 입력 할 수 있습니다.")
        Long pos
) {

}
