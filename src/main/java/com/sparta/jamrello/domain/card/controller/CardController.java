package com.sparta.jamrello.domain.card.controller;

import com.sparta.jamrello.domain.card.Service.CardServiceImplV1;
import com.sparta.jamrello.domain.card.dto.request.CardCatalogRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardPositionRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.domain.cardCollaborators.dto.CardCollaboratorRequestDto;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.annotation.AuthUser;
import com.sparta.jamrello.global.constant.ResponseCode;
import com.sparta.jamrello.global.dto.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogs/{catalogId}/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardServiceImplV1 cardService;

    @PostMapping
    public ResponseEntity<BaseResponse<CardResponseDto>> createCard(
        @PathVariable Long catalogId,
        @AuthUser Member member,
        @RequestBody CardRequestDto requestDto) {

        CardResponseDto responseDto = cardService.createCard(catalogId, member.getId(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            BaseResponse.of(ResponseCode.CREATED_CARD, responseDto)
        );
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<CardResponseDto>>> getAllCards(
        @PathVariable Long catalogId
    ) {
        List<CardResponseDto> responseDtoList = cardService.getAllCards(catalogId);

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.GET_CARD_CONTENT, responseDtoList)
        );
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<BaseResponse<CardResponseDto>> getCard(
        @PathVariable Long catalogId,
        @PathVariable Long cardId
    ) {
        CardResponseDto responseDto = cardService.getCard(cardId);

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.GET_CARD_CONTENT, responseDto)
        );
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<BaseResponse<CardResponseDto>> updateCard(
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @AuthUser Member member,   // @AuthUser 추후 수정
        @RequestBody CardRequestDto requestDto) {

        CardResponseDto responseDto = cardService.updateCard(cardId, member.getId(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.UPDATE_CARD, responseDto)
        );
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<BaseResponse<String>> deleteCard(
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @AuthUser Member member   // @AuthUser 추후 수정
    ) {
        cardService.deleteCard(cardId, member.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.DELETE_CARD, "")
        );
    }

    @PostMapping("/{cardId}/collaborators")
    public ResponseEntity<BaseResponse<String>> addCollaborator(
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @AuthUser Member member,
        @RequestBody CardCollaboratorRequestDto requestDto
    ) {
        cardService.addCollaborator(cardId, member.getId(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.INVITE_USER, "")
        );
    }

    @DeleteMapping("/{cardId}/collaborators/{collaboratorId}")
    public ResponseEntity<BaseResponse<String>> deleteCollaborator(
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @PathVariable Long collaboratorId,
        @AuthUser Member member
    ) {
        cardService.deleteCollaborator(cardId, collaboratorId, member.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.DELETE_USER, "")
        );
    }

    @PatchMapping("/{cardId}/move")
    public ResponseEntity<BaseResponse<String>> changeCardCatalog(
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @AuthUser Member member,
        @RequestBody CardCatalogRequestDto requestDto
    ) {
        cardService.changeCardCatalog(cardId, member.getId(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.MOVE_CARD_POSITION, "")
        );
    }

    @PatchMapping("/{cardId}/pos")
    public ResponseEntity<BaseResponse<String>> updateCardPos(
        @PathVariable Long catalogId,
        @PathVariable Long cardId,
        @AuthUser Member member,
        @RequestBody CardPositionRequestDto requestDto
    ) {
        cardService.updateCardPos(catalogId, cardId, member.getId(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.MOVE_CARD_POSITION, "")
        );
    }
}
