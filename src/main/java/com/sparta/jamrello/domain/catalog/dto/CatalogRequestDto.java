package com.sparta.jamrello.domain.catalog.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CatalogRequestDto(
        @NotEmpty(message = "제목은 공백 및 빈 문자열로 생성할 수 없습니다.")
        @Size(min = 1, max = 255, message = "제목은 255자 까지 입력 할 수 있습니다.")
        String title
) {

}