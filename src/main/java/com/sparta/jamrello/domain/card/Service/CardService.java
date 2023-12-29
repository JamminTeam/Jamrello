package com.sparta.jamrello.domain.card.Service;


import com.sparta.jamrello.domain.card.dto.request.CardCatalogRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardPositionRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.domain.cardCollaborators.dto.CardCollaboratorRequestDto;
import java.util.List;

public interface CardService {

    CardResponseDto createCard(Long catalogId, Long memberId, CardRequestDto requestDto);

    List<CardResponseDto> getAllCards(Long catalogId);

    CardResponseDto getCard(Long cardId);

    CardResponseDto updateCard(Long cardId, Long memberId, CardRequestDto requestDto);

    void deleteCard(Long cardId, Long memberId);

    void addCollaborator(Long cardId, Long memberId, CardCollaboratorRequestDto requestDto);

    void deleteCollaborator(Long cardId, Long collaboratorId, Long memberId);

    void changeCardCatalog(Long cardId, Long memberId, CardCatalogRequestDto requestDto);

    void updateCardPos(Long cardId, Long memberId, CardPositionRequestDto requestDto);
}
