package com.sparta.jamrello.domain.card.Service;


import com.sparta.jamrello.domain.card.dto.request.CardCatalogRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardPositionRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.domain.cardCollaborators.dto.CardCollaboratorRequestDto;
import com.sparta.jamrello.global.dto.BaseResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface CardService {

    ResponseEntity<BaseResponse<CardResponseDto>> createCard(Long catalogId, Long memberId,
        CardRequestDto requestDto);

    ResponseEntity<BaseResponse<List<CardResponseDto>>> getAllCards(Long catalogId);

    ResponseEntity<BaseResponse<CardResponseDto>> getCard(Long cardId);

    ResponseEntity<BaseResponse<CardResponseDto>> updateCard(Long cardId, Long memberId,
        CardRequestDto requestDto);

    ResponseEntity<BaseResponse<String>> deleteCard(Long cardId, Long memberId);

    ResponseEntity<BaseResponse<String>> addCollaborator(Long cardId, Long memberId,
        CardCollaboratorRequestDto requestDto);

    ResponseEntity<BaseResponse<String>> deleteCollaborator(Long cardId, Long collaboratorId,
        Long memberId);

    ResponseEntity<BaseResponse<String>> changeCardCatalog(Long cardId, Long memberId,
        CardCatalogRequestDto requestDto);

    ResponseEntity<BaseResponse<String>> updateCardPos(Long catalogId, Long cardId, Long memberId,
        CardPositionRequestDto requestDto);
}
