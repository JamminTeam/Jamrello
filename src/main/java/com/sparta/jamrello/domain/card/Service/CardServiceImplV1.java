package com.sparta.jamrello.domain.card.Service;

import com.sparta.jamrello.domain.card.dto.request.CreateCardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CreateCardResponseDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImplV1 implements CardService {

    private final CardRepository cardRepository;
    private final CatalogRepository catalogRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<BaseResponse> createCard(Long catalogId, Long memberId,
        CreateCardRequestDto requestDto) {
        Catalog catalog = findCatalog(catalogId);
        Member member = findMember(memberId);

        Card card = cardRepository.save(requestDto.toEntity(member, catalog));

        return ResponseEntity.status(200).body(
            new BaseResponse(
                ResponseCode.CREATED_CARD.getMessage(),
                ResponseCode.CREATED_CARD.getHttpStatus(),
                new CreateCardResponseDto(card.getTitle()))
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
}
