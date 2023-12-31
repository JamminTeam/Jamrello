package com.sparta.jamrello.domain.card.dto.request;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record CardDuedateRequestDto(
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime startDay,
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime dueDay
) {

}
