package com.sparta.jamrello.domain.board.dto.response;

public record BoardResponseDto(
        String title,
        String username,
        String description,
        String backgroundColor,
        boolean status
) {

}
