package com.sparta.jamrello.domain.card.controller;

import com.sparta.jamrello.domain.card.Service.CardServiceImplV1;
import com.sparta.jamrello.domain.card.dto.request.CreateCardRequestDto;
import com.sparta.jamrello.global.dto.BaseResponse;
import com.sparta.jamrello.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<BaseResponse> createCard(
        @PathVariable Long catalogId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,   // @AuthUser 추후 수정
        @RequestBody CreateCardRequestDto requestDto) {
        return cardService.createCard(catalogId, userDetails.getMember().getId(), requestDto);
    }
}
