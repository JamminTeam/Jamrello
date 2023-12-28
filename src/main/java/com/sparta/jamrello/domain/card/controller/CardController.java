package com.sparta.jamrello.domain.card.controller;

import com.sparta.jamrello.domain.card.Service.CardServiceImplV1;
import com.sparta.jamrello.domain.card.dto.request.CardCatalogRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.domain.cardCollaborators.dto.CardCollaboratorRequestDto;
import com.sparta.jamrello.global.dto.BaseResponse;
import com.sparta.jamrello.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards/{boardId}/catalogs/{catalogId}/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardServiceImplV1 cardService;

    @PostMapping
    public ResponseEntity<BaseResponse<CardResponseDto>> createCard(
        @PathVariable Long boardId,
        @PathVariable Long catalogId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,   // @AuthUser 추후 수정
        @RequestBody CardRequestDto requestDto) {
        return cardService.createCard(catalogId, userDetails.getMember().getId(), requestDto);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<CardResponseDto>>> getAllCards(
        @PathVariable Long boardId,
        @PathVariable Long catalogId
    ) {
        return cardService.getAllCards(catalogId);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<BaseResponse<CardResponseDto>> getCard(
        @PathVariable Long boardId,
        @PathVariable Long catalogId,
        @PathVariable Long cardId
    ) {
        return cardService.getCard(cardId);
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<BaseResponse<CardResponseDto>> updateCard(
        @PathVariable Long boardId,
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,   // @AuthUser 추후 수정
        @RequestBody CardRequestDto requestDto) {
        return cardService.updateCard(cardId, userDetails.getMember().getId(), requestDto);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<BaseResponse<String>> deleteCard(
        @PathVariable Long boardId,
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails   // @AuthUser 추후 수정
    ) {
        return cardService.deleteCard(cardId, userDetails.getMember().getId());
    }

    @PostMapping("/{cardId}/collaborators")
    public ResponseEntity<BaseResponse<String>> addCollaborator(
        @PathVariable Long boardId,
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody CardCollaboratorRequestDto requestDto
    ) {
        return cardService.addCollaborator(cardId, userDetails.getMember().getId(), requestDto);
    }

    @DeleteMapping("/{cardId}/collaborators/{collaboratorId}")
    public ResponseEntity<BaseResponse<String>> deleteCollaborator(
        @PathVariable Long boardId,
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @PathVariable Long collaboratorId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return cardService.deleteCollaborator(cardId, collaboratorId,
            userDetails.getMember().getId());
    }

    @PatchMapping("/{cardId}/move")
    public ResponseEntity<BaseResponse<String>> changeCardCatalog(
        @PathVariable Long boardId,
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody CardCatalogRequestDto requestDto
    ) {
        return cardService.changeCardCatalog(cardId, userDetails.getMember().getId(), requestDto);
    }
}
