package com.sparta.jamrello.domain.card.Service;


import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CreateCardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.global.dto.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface CardService {

    ResponseEntity<BaseResponse<CardResponseDto>> createCard(Long catalogId, Long memberId,
        CreateCardRequestDto requestDto);

    ResponseEntity<BaseResponse<CardResponseDto>> updateCard(Long cardId, Long memberId,
        CardRequestDto requestDto);
}
