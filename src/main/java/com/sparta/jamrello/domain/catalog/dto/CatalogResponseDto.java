package com.sparta.jamrello.domain.catalog.dto;

public record CatalogResponseDto(
        Long id,
        String title,
        String createdAt,
        Long position
) {

}
