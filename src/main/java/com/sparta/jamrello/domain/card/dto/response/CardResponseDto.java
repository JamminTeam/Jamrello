package com.sparta.jamrello.domain.card.dto.response;

public record CardResponseDto(
    String title,
    String username,
    String description,
    String backgroundColor
) {

    public CardResponseDto(String title) {
        this(title, null, null, null);
    }
}
