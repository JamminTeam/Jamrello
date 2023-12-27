package com.sparta.jamrello.domain.card.dto.request;

public record CardRequestDto(
    String title,
    String description,
    String backgroundColor
) {

    public CardRequestDto(String title) {
        this(title, null, null);
    }
}
