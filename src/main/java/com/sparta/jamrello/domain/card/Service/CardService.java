package com.sparta.jamrello.domain.card.Service;


import com.sparta.jamrello.domain.card.dto.request.CreateCardRequestDto;
import com.sparta.jamrello.global.dto.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface CardService {

    ResponseEntity<BaseResponse> createCard(Long catalogId, Long id,
        CreateCardRequestDto requestDto);
}
