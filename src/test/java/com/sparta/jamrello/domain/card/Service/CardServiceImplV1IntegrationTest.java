package com.sparta.jamrello.domain.card.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.board.repository.BoardRepository;
import com.sparta.jamrello.domain.card.dto.request.CardDuedateRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardPositionRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.cardCollaborators.dto.CardCollaboratorRequestDto;
import com.sparta.jamrello.domain.cardCollaborators.repository.CardCollaboratorRepository;
import com.sparta.jamrello.domain.cardCollaborators.repository.entity.CardCollaborator;
import com.sparta.jamrello.domain.catalog.repository.CatalogRepository;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @BeforeEach
    void setUp() {
        member = new Member("user1", "password", "user1", "user@email.com");
        board = new Board("제목", "#000000");
        catalog = new Catalog("제목", 1L, board, true);
        cardRequestDto = new CardRequestDto("제목", null, null);

        memberRepository.save(member);
        boardRepository.save(board);
        catalogRepository.save(catalog);
    }

    @AfterEach
    void tearDown() {
        cardRepository.deleteAll();
        catalogRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("카드 삭제 성공")
    void deleteCardTest_success() {
        // given
        CardResponseDto card = cardService.createCard(catalog.getId(), member.getId(),
            cardRequestDto);

        // when
        cardService.deleteCard(card.id(), member.getId());
        Card deletedCard = cardRepository.findById(card.id()).orElse(null);

        // then
        assertNull(deletedCard);
    }

    @Test
    @DisplayName("카드 보관 성공")
    @Transactional
    void keepCardTest() {
        // given
        CardResponseDto card = cardService.createCard(catalog.getId(), member.getId(),
            cardRequestDto);

        // when
        cardService.keepCard(card.id(), member.getId());
        Card updatedCard = cardRepository.findById(card.id()).orElse(null);

        // then
        assertTrue(updatedCard.isStatus());
    }

    @Test
    @DisplayName("카드 순서 이동 성공")
    @Transactional
    void updateCardPosTest_success() {
        // given
        CardPositionRequestDto cardPositionRequestDto = new CardPositionRequestDto(2L);
        CardResponseDto card1 = cardService.createCard(catalog.getId(), member.getId(),
            cardRequestDto);
        CardResponseDto card2 = cardService.createCard(catalog.getId(), member.getId(),
            cardRequestDto);
        CardResponseDto card3 = cardService.createCard(catalog.getId(), member.getId(),
            cardRequestDto);

        // when
        cardService.updateCardPos(card1.id(), member.getId(), cardPositionRequestDto);

        Card response1 = cardRepository.findById(card1.id()).get();
        Card response2 = cardRepository.findById(card2.id()).get();
        Card response3 = cardRepository.findById(card3.id()).get();

        // then
        assertEquals(2L, response1.getPosition());
        assertEquals(1L, response2.getPosition());
        assertEquals(3L, response3.getPosition());
    }

    @Nested
    @DisplayName("카드 생성 테스트")
    class createCardTest {

        @Test
        @DisplayName("카드 생성 성공")
        @Transactional
        void createCardTest_success() {
            // when
            CardResponseDto response = cardService.createCard(catalog.getId(), member.getId(),
                cardRequestDto);

            // then
            assertEquals(cardRequestDto.title(), response.title());
            assertEquals(response.title(), catalog.getCardList().get(0).getTitle());
        }

        @Test
        @DisplayName("카드 생성 실패 - 카탈로그 없음")
        void createCardTest_NotFoundCatalog() {
            // when
            BisException e = assertThrows(BisException.class, () -> {
                cardService.createCard(100L, member.getId(), cardRequestDto);
            });

            // then
            assertEquals(ErrorCode.NOT_FOUND_CATALOG, e.getErrorCode());
        }

        @Test
        @DisplayName("카드 생성 실패 - 멤버 없음")
        void createCardTest_NotFoundMember() {
            // when
            BisException e = assertThrows(BisException.class, () -> {
                cardService.createCard(catalog.getId(), 100L, cardRequestDto);
            });

            // then
            assertEquals(ErrorCode.NOT_FOUND_MEMBER, e.getErrorCode());
        }
    }

    @Nested
    @DisplayName("카드 조회 테스트")
    class getCardTest {

        @Test
        @DisplayName("카드 조회 성공")
        @Transactional
        void getCardTest_success() {
            // given
            CardResponseDto card = cardService.createCard(catalog.getId(), member.getId(),
                cardRequestDto);

            // when
            CardResponseDto response = cardService.getCard(card.id());

            // then
            assertEquals("제목", response.title());
        }

        @Test
        @DisplayName("카드 조회 실패 - 카드 없음")
        void getCardTest_NotFoundCard() {
            // when
            BisException e = assertThrows(BisException.class, () -> {
                cardService.getCard(100L);
            });

            // then
            assertEquals(ErrorCode.NOT_FOUND_CARD, e.getErrorCode());
        }
    }

    @Nested
    @DisplayName("카드 수정 테스트")
    class updateCardTest {

        @Test
        @DisplayName("카드 수정 성공")
        void updateCardTest_success() {
            // given
            CardResponseDto card = cardService.createCard(catalog.getId(), member.getId(),
                cardRequestDto);
            CardRequestDto updateRequestDto = new CardRequestDto("제목 수정", "설명", "#FFFFFF");

            // when
            CardResponseDto response = cardService.updateCard(card.id(), member.getId(),
                updateRequestDto);

            // then
            assertEquals("제목 수정", response.title());
            assertEquals("설명", response.description());
            assertEquals("#FFFFFF", response.backgroundColor());
        }

        @Test
        @DisplayName("카드 기한 수정 성공")
        void updateCardDueDayTest_success() {
            // given
            CardResponseDto card = cardService.createCard(catalog.getId(), member.getId(),
                cardRequestDto);
            LocalDateTime now = LocalDateTime.now();
            CardDuedateRequestDto updateDueDayRequestDto = new CardDuedateRequestDto(
                now, now);

            // when
            CardResponseDto response = cardService.updateCardDueDay(card.id(), member.getId(),
                updateDueDayRequestDto);

            // then
            assertEquals(
                now.format((DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                response.startDay());
        }

        @Test
        @DisplayName("카드 수정 실패 - 권한 없음")
        void updateCardTest_rejected() {
            // given
            CardResponseDto card = cardService.createCard(catalog.getId(), member.getId(),
                cardRequestDto);
            CardRequestDto updateRequestDto = new CardRequestDto("제목 수정", "설명", "#FFFFFF");

            // when
            BisException e = assertThrows(BisException.class, () -> {
                cardService.updateCard(card.id(), 100L, updateRequestDto);
            });

            // then
            assertEquals(ErrorCode.YOUR_NOT_COME_IN, e.getErrorCode());
        }
    }

    @Nested
    @DisplayName("작업자 추가 테스트")
    @Transactional
    class addCollaboratorTest {

        @Test
        @DisplayName("작업자 추가 성공")
        void addCollaboratorTest_success() {
            // given
            CardResponseDto cardResponse = cardService.createCard(catalog.getId(), member.getId(),
                cardRequestDto);
            Card card = cardRepository.findById(cardResponse.id()).orElse(null);
            Member collaborator = new Member("col", "pass", "col", "col@email");
            CardCollaboratorRequestDto colRequestDto = new CardCollaboratorRequestDto("col");

            memberRepository.save(collaborator);

            // when
            cardService.addCollaborator(cardResponse.id(), member.getId(), colRequestDto);

            // then
            assertEquals(collaborator.getId(),
                card.getCardCollaboratorList().get(0).getMember().getId());
        }

        @Test
        @DisplayName("작업자 추가 실패 - 이미 존재하는 작업자")
        void addCollaboratorTest_alreadyExist() {
            // given
            CardResponseDto cardResponse = cardService.createCard(catalog.getId(), member.getId(),
                cardRequestDto);
            Card card = cardRepository.findById(cardResponse.id()).orElse(null);
            Member collaborator = new Member("col", "pass", "col", "col@email");
            CardCollaborator cardCollaborator = new CardCollaborator(collaborator, card);
            CardCollaboratorRequestDto colRequestDto = new CardCollaboratorRequestDto("col");

            memberRepository.save(collaborator);
            cardCollaboratorRepository.save(cardCollaborator);

            // when
            BisException e = assertThrows(BisException.class, () -> {
                cardService.addCollaborator(cardResponse.id(), member.getId(), colRequestDto);
            });

            // then
            assertEquals(ErrorCode.ALREADY_EXIST_COLLABORATOR, e.getErrorCode());
        }
    }

    @Nested
    @DisplayName("작업자 삭제 테스트")
    class deleteCollaboratorTest {

        @Test
        @DisplayName("작업자 삭제 성공")
        void deleteCollaboratorTest_success() {
            // given
            CardResponseDto cardResponse = cardService.createCard(catalog.getId(), member.getId(),
                cardRequestDto);
            Card card = cardRepository.findById(cardResponse.id()).orElse(null);
            CardCollaboratorRequestDto colRequestDto = new CardCollaboratorRequestDto("col");
            Member collaborator = new Member("col", "pass", "col", "col@email");

            memberRepository.save(collaborator);

            cardService.addCollaborator(cardResponse.id(), member.getId(), colRequestDto);

            // when
            cardService.deleteCollaborator(cardResponse.id(), collaborator.getId(), member.getId());

            // then
            CardCollaborator cardCollaborator = cardCollaboratorRepository
                .findByCardIdAndMemberId(cardResponse.id(), collaborator.getId()).orElse(null);
            assertNull(cardCollaborator);
        }

        @Test
        @DisplayName("작업자 삭제 실패 - 존재하지않는 작업자")
        void deleteCollaboratorTest_notFound() {
            // given
            CardResponseDto cardResponse = cardService.createCard(catalog.getId(), member.getId(),
                cardRequestDto);
            Member collaborator = new Member("col", "pass", "col", "col@email");

            memberRepository.save(collaborator);

            // when
            BisException e = assertThrows(BisException.class, () -> {
                cardService.deleteCollaborator(cardResponse.id(), collaborator.getId(),
                    member.getId());
            });

            // then
            assertEquals(ErrorCode.NOT_FOUND_COLLABORATOR, e.getErrorCode());
        }
    }
}
