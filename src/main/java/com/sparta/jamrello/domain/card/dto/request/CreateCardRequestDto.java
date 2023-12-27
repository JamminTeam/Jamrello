package com.sparta.jamrello.domain.card.dto.request;

import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.member.repository.entity.Member;

public record CreateCardRequestDto(
    String title
) {

    public Card toEntity(Member member, Catalog catalog) {
        return Card.builder()
            .title(title)
            .member(member)
            .catalog(catalog)
            .build();
    }
}
