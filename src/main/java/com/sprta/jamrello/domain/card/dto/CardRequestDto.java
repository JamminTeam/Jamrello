package com.sprta.jamrello.domain.card.dto;

import com.sprta.jamrello.domain.card.repository.entity.Card;

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
