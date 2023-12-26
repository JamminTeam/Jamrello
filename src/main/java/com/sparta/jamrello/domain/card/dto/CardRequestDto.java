package com.sparta.jamrello.domain.card.dto;

import com.sparta.jamrello.domain.card.repository.entity.Card;

public record CardRequestDto(
        String title,
        String description
) {

    public Card toEntity() {
        return Card.builder()
                .title(title)
                .description(description)
                .build();
    }
}
