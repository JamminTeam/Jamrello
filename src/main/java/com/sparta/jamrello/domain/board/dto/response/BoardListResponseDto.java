package com.sparta.jamrello.domain.board.dto.response;

import java.util.List;

public record BoardListResponseDto(
    Long id,
    String title,
    List<getFromCardListDto> fromCardListDtoList
) {

}
