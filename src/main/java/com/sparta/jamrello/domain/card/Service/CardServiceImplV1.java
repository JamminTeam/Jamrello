package com.sparta.jamrello.domain.card.Service;

import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.catalog.repository.entity.CatalogRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.member.repository.entity.MemberRepository;
import com.sparta.jamrello.global.constant.ResponseCode;
import com.sparta.jamrello.global.dto.BaseResponse;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardServiceImplV1 implements CardService {

    private final CardRepository cardRepository;
    private final CatalogRepository catalogRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<BaseResponse<CardResponseDto>> createCard(Long catalogId, Long memberId,
        CardRequestDto requestDto) {

        Catalog catalog = findCatalog(catalogId);
        Member member = findMember(memberId);
        Card card = Card.builder().title(requestDto.title()).member(member).catalog(catalog)
            .build();
        cardRepository.save(card);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            BaseResponse.of(ResponseCode.CREATED_CARD, new CardResponseDto(card.getTitle()))
        );
    }

    public ResponseEntity<BaseResponse<CardResponseDto>> getCard(Long cardId) {
        Card card = findCard(cardId);
        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.GET_CARD_CONTENT, new CardResponseDto(card.getTitle()))
        );
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<CardResponseDto>> updateCard(Long cardId, Long memberId,
        CardRequestDto requestDto) {

        Card card = findCard(cardId);
        checkMember(memberId, card);
        card.update(requestDto);
        CardResponseDto responseDto = new CardResponseDto(
            card.getTitle(), card.getMember().getNickname(),
            card.getDescription(), card.getBackgroundColor()
        );

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.UPDATE_CARD, responseDto)
        );
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<String>> deleteCard(Long cardId, Long memberId) {

        Card card = findCard(cardId);
        checkMember(memberId, card);
        cardRepository.delete(card);

        return ResponseEntity.status(HttpStatus.OK).body(
            BaseResponse.of(ResponseCode.DELETE_CARD, "")
        );
    }

    private Card findCard(Long id) {
        return cardRepository.findById(id).orElseThrow(() ->
            new BisException(ErrorCode.NOT_FOUND_CARD)
        );
    }

    private Catalog findCatalog(Long id) {
        return catalogRepository.findById(id).orElseThrow(() ->
            new BisException(ErrorCode.NOT_FOUND_CATALOG)
        );
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() ->
            new BisException(ErrorCode.NOT_FOUND_MEMBER)
        );
    }

    private void checkMember(Long id, Card card) {
        if (!id.equals(card.getMember().getId())) {
            throw new BisException(ErrorCode.REJECTED_EXECUSION);
        }
    }
}
