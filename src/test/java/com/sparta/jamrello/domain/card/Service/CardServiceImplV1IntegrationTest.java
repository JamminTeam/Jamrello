package com.sparta.jamrello.domain.card.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.board.repository.BoardRepository;
import com.sparta.jamrello.domain.card.dto.request.CardPositionRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.cardCollaborators.repository.CardCollaboratorRepository;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.catalog.repository.entity.CatalogRepository;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class CardServiceImplV1IntegrationTest {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CatalogRepository catalogRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CardCollaboratorRepository cardCollaboratorRepository;

    @Autowired
    CardServiceImplV1 cardService;

    Member member;
    Catalog catalog;
    Board board;
    CardRequestDto cardRequestDto;
    Card card;

    @BeforeEach
    void setUp() {
        member = new Member("user1", "password", "user1", "user1@email.com");
        board = new Board("제목", "#000000");
        catalog = new Catalog("제목", board);
        cardRequestDto = new CardRequestDto("제목", null, null);
        card = new Card(cardRequestDto.title(), member, catalog);

        memberRepository.save(member);
        boardRepository.save(board);
        catalogRepository.save(catalog);
        cardRepository.save(card);
    }

    @Test
    @DisplayName("카드 순서 이동 성공")
    @Transactional
    void updateCardPosTest() {
        // given
        CardPositionRequestDto cardPositionRequestDto = new CardPositionRequestDto(2L);
        Card card1 = new Card(cardRequestDto.title(), member, catalog);
        cardRepository.save(card1);

        card.setPosition(1L);
        card1.setPosition(2L);
        catalog.getCardList().add(card);
        catalog.getCardList().add(card1);

        // when
        cardService.updateCardPos(1L, 1L, cardPositionRequestDto);

        Card response1 = cardRepository.findById(1L).get();
        Card response2 = cardRepository.findById(2L).get();

        // then
        assertEquals(2L, response1.getPosition());
        assertEquals(1L, response2.getPosition());
    }
}
