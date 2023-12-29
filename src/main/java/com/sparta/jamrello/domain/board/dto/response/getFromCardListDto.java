package com.sparta.jamrello.domain.board.dto.response;

public record getFromCardListDto(
    String title,
    String imageUrl,
    String backgroundColor,
    int commentCount,
    int cardCollaboratorCount
) {

}
