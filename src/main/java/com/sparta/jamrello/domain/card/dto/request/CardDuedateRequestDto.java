package com.sparta.jamrello.domain.card.dto.request;

import java.time.LocalDateTime;

public record CardDuedateRequestDto(
    LocalDateTime startDay,
    LocalDateTime dueDay
) {

}
