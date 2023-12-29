package com.sparta.jamrello.domain.card.dto.response;

public record CardResponseDto(
    
    Long id,
    String title,
    String username,
    String description,
    String backgroundColor
) {

}
