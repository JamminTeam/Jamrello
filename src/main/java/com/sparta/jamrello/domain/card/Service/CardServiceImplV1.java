package com.sparta.jamrello.domain.card.Service;

import com.sparta.jamrello.domain.card.dto.request.CardCatalogRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardPositionRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.cardCollaborators.dto.CardCollaboratorRequestDto;
import com.sparta.jamrello.domain.cardCollaborators.repository.CardCollaboratorRepository;
import com.sparta.jamrello.domain.cardCollaborators.repository.entity.CardCollaborator;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.catalog.repository.entity.CatalogRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.member.repository.entity.MemberRepository;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardServiceImplV1 implements CardService {

    private final CardRepository cardRepository;
    private final CatalogRepository catalogRepository;
    private final MemberRepository memberRepository;
    private final CardCollaboratorRepository cardCollaboratorRepository;

    @Override
    @Transactional
    public CardResponseDto createCard(Long catalogId, Long memberId,
        CardRequestDto requestDto) {

        Catalog catalog = findCatalog(catalogId);
        Member member = findMember(memberId);
        Card card = Card.builder()
            .title(requestDto.title()).member(member).catalog(catalog).build();
        card.setPosition((long) (catalog.getCardList().size() + 1));

        cardRepository.save(card);
        catalog.getCardList().add(card);

        return new CardResponseDto(card.getTitle(), null, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardResponseDto> getAllCards(Long catalogId) {

        Catalog catalog = findCatalog(catalogId);

        List<Card> cardList = catalog.getCardList();
        cardList.sort(Comparator.comparing(Card::getPosition));

        return cardList.stream()
            .map(card -> new CardResponseDto(
                card.getTitle(),
                card.getMember().getUsername(),
                card.getDescription(),
                card.getBackgroundColor()
            )).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CardResponseDto getCard(Long cardId) {
        Card card = findCard(cardId);
        return new CardResponseDto(card.getTitle(), null, null, null);
    }

    @Override
    @Transactional
    public CardResponseDto updateCard(Long cardId, Long memberId, CardRequestDto requestDto) {

        Card card = findCard(cardId);
        checkMember(memberId, card);
        card.update(requestDto);

        return new CardResponseDto(
            card.getTitle(), card.getMember().getUsername(),
            card.getDescription(), card.getBackgroundColor()
        );
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId, Long memberId) {

        Card card = findCard(cardId);
        checkMember(memberId, card);
        cardRepository.delete(card);


    }

    @Override
    @Transactional
    public void addCollaborator(Long cardId, Long memberId, CardCollaboratorRequestDto requestDto) {

        Card card = findCard(cardId);
        checkMember(memberId, card);
        Member collaborator = memberRepository.findByUsername(requestDto.username())
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_MEMBER));

        Optional<CardCollaborator> cardCol =
            cardCollaboratorRepository.findByCardIdAndMemberId(cardId, collaborator.getId());
        if (cardCol.isPresent()) {
            throw new BisException(ErrorCode.ALREADY_EXIST_COLLABORATOR);
        }

        CardCollaborator cardCollaborator = CardCollaborator.builder()
            .member(collaborator).card(card).build();
        cardCollaboratorRepository.save(cardCollaborator);
        card.getCardCollaboratorList().add(cardCollaborator);
    }

    @Override
    @Transactional
    public void deleteCollaborator(Long cardId, Long collaboratorId, Long memberId) {

        CardCollaborator cardCollaborator = cardCollaboratorRepository.findByCardIdAndMemberId(
            cardId, memberId).orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_COLLABORATOR));
        cardCollaboratorRepository.delete(cardCollaborator);
    }

    @Override
    @Transactional
    public void changeCardCatalog(Long cardId, Long memberId, CardCatalogRequestDto requestDto) {

        Card card = findCard(cardId);
        Catalog catalog = findCatalog(requestDto.catalogId());
        card.setCatalog(catalog);
    }

    @Override
    @Transactional
    public void updateCardPos(Long catalogId, Long cardId, Long memberId,
        CardPositionRequestDto requestDto) {

        Catalog catalog = findCatalog(catalogId);

        if (requestDto.pos() > catalog.getCardList().size() || requestDto.pos() < 1) {
            throw new BisException(ErrorCode.POSITION_OVER);
        }

        Card card = findCard(cardId);
        checkMember(memberId, card);

        Long currentPos = card.getPosition();
        Long changePos = requestDto.pos();

        if (changePos > currentPos) {
            cardRepository.decreasePositionBeforeUpdate(catalogId, currentPos, changePos);
        } else {
            cardRepository.increasePositionBeforeUpdate(catalogId, currentPos, changePos);
        }

        cardRepository.updateCardPosition(cardId, changePos);
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
        if (!id.equals(card.getMember().getId()) &&
            card.getCardCollaboratorList().stream()
                .noneMatch(collaborator -> id.equals(collaborator.getMember().getId()))) {
            throw new BisException(ErrorCode.REJECTED_EXECUSION);
        }
    }
}
