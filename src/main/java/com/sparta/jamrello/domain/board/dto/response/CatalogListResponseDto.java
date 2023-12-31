package com.sparta.jamrello.domain.board.dto.response;

import java.util.List;

public record CatalogListResponseDto(
    Long id,
    String title,
    List<GetFromCardListDto> fromCardListDtoList
) {

}
