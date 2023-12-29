package com.sparta.jamrello.domain.card.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.card.dto.request.CardCatalogRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.cardCollaborators.dto.CardCollaboratorRequestDto;
import com.sparta.jamrello.domain.cardCollaborators.repository.CardCollaboratorRepository;
import com.sparta.jamrello.domain.cardCollaborators.repository.entity.CardCollaborator;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.catalog.repository.entity.CatalogRepository;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CardServiceImplV1Test {

    @Mock
    CardRepository cardRepository;

    @Mock
    CatalogRepository catalogRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    CardCollaboratorRepository cardCollaboratorRepository;

    @InjectMocks
    CardServiceImplV1 cardService;

    Member member;
    Catalog catalog;
    Board board;
    CardRequestDto cardRequestDto;
    Card card;

    @BeforeEach
    void setUp() {
        member = new Member("user1", "password", "user1", "user1@email.com");
        member.setId(1L);
        board = new Board();
        catalog = new Catalog("제목", board);
        cardRequestDto = new CardRequestDto("제목", null, null);
        card = new Card(cardRequestDto.title(), member, catalog);
    }

    @Test
    @DisplayName("카드 생성 성공")
    void createCardTest_success() {
        // given
        when(catalogRepository.findById(anyLong())).thenReturn(Optional.of(catalog));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        // when
        CardResponseDto response = cardService.createCard(1L, 1L, cardRequestDto);

        // then
        assertEquals(card.getId(), catalog.getCardList().get(0).getId());
    }

    @Test
    @DisplayName("카드 생성 실패 - 카탈로그 없음")
    void createCardTest_NotFoundCatalog() {
        // given
        when(catalogRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        BisException e = assertThrows(BisException.class, () -> {
            cardService.createCard(1L, 1L, cardRequestDto);
        });

        // then
        assertEquals(ErrorCode.NOT_FOUND_CATALOG, e.getErrorCode());
    }

    @Test
    @DisplayName("카드 생성 실패 - 멤버 없음")
    void createCardTest_NotFoundMember() {
        // given
        when(catalogRepository.findById(anyLong())).thenReturn(Optional.of(catalog));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        BisException e = assertThrows(BisException.class, () -> {
            cardService.createCard(1L, 1L, cardRequestDto);
        });

        // then
        assertEquals(ErrorCode.NOT_FOUND_MEMBER, e.getErrorCode());
    }

    @Test
    @DisplayName("카드 조회 성공")
    void getCardTest_success() {
        // given
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        // when
        CardResponseDto response = cardService.getCard(1L);

        // then
        assertEquals("제목", response.title());
    }

    @Test
    @DisplayName("카드 조회 실패 - 카드 없음")
    void getCardTest_NotFoundCard() {
        // given
        when(cardRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        BisException e = assertThrows(BisException.class, () -> {
            cardService.getCard(1L);
        });

        // then
        assertEquals(ErrorCode.NOT_FOUND_CARD, e.getErrorCode());
    }

    @Test
    @DisplayName("카드 수정 성공")
    void updateCardTest_success() {
        // given
        cardRequestDto = new CardRequestDto("제목 수정", "설명", "#FFFFFF");
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        // when
        CardResponseDto response = cardService.updateCard(1L, 1L, cardRequestDto);

        // then
        assertEquals("제목 수정", response.title());
        assertEquals("설명", response.description());
        assertEquals("#FFFFFF", response.backgroundColor());
    }

    @Test
    @DisplayName("카드 수정 실패 - 권한 없음")
    void updateCardTest_rejected() {
        // given
        cardRequestDto = new CardRequestDto("제목 수정", "설명", "#FFFFFF");
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        // when
        BisException e = assertThrows(BisException.class, () -> {
            cardService.updateCard(1L, 2L, cardRequestDto);
        });

        // then
        assertEquals(ErrorCode.REJECTED_EXECUSION, e.getErrorCode());
    }

    @Test
    @DisplayName("카드 삭제 성공")
    void deleteCardTest() {
        // given
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        // when
        cardService.deleteCard(1L, 1L);

        // then
        verify(cardRepository, times(1)).delete(any(Card.class));
    }

    @Test
    @DisplayName("작업자 추가 성공")
    void addCollaboratorTest_success() {
        // given
        Member collaborator = new Member("col", "pass", "col", "col@email");
        CardCollaborator cardCollaborator = new CardCollaborator(collaborator, card);
        CardCollaboratorRequestDto colRequestDto = new CardCollaboratorRequestDto("col");
        collaborator.setId(1L);

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(collaborator));
        when(cardCollaboratorRepository.findByCardIdAndMemberId(anyLong(), anyLong()))
            .thenReturn(Optional.empty());
        when(cardCollaboratorRepository.save(any(CardCollaborator.class)))
            .thenReturn(cardCollaborator);

        // when
        cardService.addCollaborator(1L, 1L, colRequestDto);

        // then
        assertEquals(collaborator.getId(),
            card.getCardCollaboratorList().get(0).getMember().getId());
    }

    @Test
    @DisplayName("작업자 추가 실패 - 이미 존재하는 작업자")
    void addCollaboratorTest_alreadyExist() {
        // given
        Member collaborator = new Member("col", "pass", "col", "col@email");
        CardCollaborator cardCollaborator = new CardCollaborator(collaborator, card);
        CardCollaboratorRequestDto colRequestDto = new CardCollaboratorRequestDto("col");
        collaborator.setId(1L);

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(collaborator));
        when(cardCollaboratorRepository.findByCardIdAndMemberId(anyLong(), anyLong()))
            .thenReturn(Optional.of(cardCollaborator));

        // when
        BisException e = assertThrows(BisException.class, () -> {
            cardService.addCollaborator(1L, 1L, colRequestDto);
        });

        // then
        assertEquals(ErrorCode.ALREADY_EXIST_COLLABORATOR, e.getErrorCode());
    }

    @Test
    @DisplayName("작업자 삭제 성공")
    void deleteCollaboratorTest_success() {
        // given
        Member collaborator = new Member("col", "pass", "col", "col@email");
        CardCollaborator cardCollaborator = new CardCollaborator(collaborator, card);
        collaborator.setId(1L);

        when(cardCollaboratorRepository.findByCardIdAndMemberId(anyLong(), anyLong()))
            .thenReturn(Optional.of(cardCollaborator));

        // when
        cardService.deleteCollaborator(1L, 1L, 1L);

        // then
        verify(cardCollaboratorRepository, times(1))
            .delete(any(CardCollaborator.class));
    }

    @Test
    @DisplayName("작업자 삭제 실패 - 존재하지않는 작업자")
    void deleteCollaboratorTest_notFound() {
        // given
        Member collaborator = new Member("col", "pass", "col", "col@email");
        CardCollaborator cardCollaborator = new CardCollaborator(collaborator, card);
        collaborator.setId(1L);

        when(cardCollaboratorRepository.findByCardIdAndMemberId(anyLong(), anyLong()))
            .thenReturn(Optional.empty());

        // when
        BisException e = assertThrows(BisException.class, () -> {
            cardService.deleteCollaborator(1L, 1L, 1L);
        });

        // then
        assertEquals(ErrorCode.NOT_FOUND_COLLABORATOR, e.getErrorCode());
    }

    @Test
    @DisplayName("카드 카탈로그 이동 성공")
    void changeCardCatalogTest() {
        // given
        Catalog catalog1 = new Catalog("new catalog", board);
        catalog1.setId(2L);
        CardCatalogRequestDto cardCatalogRequestDto = new CardCatalogRequestDto(2L);

        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));
        when(catalogRepository.findById(anyLong())).thenReturn(Optional.of(catalog1));

        // when
        cardService.changeCardCatalog(1L, 1L, cardCatalogRequestDto);

        // then
        assertEquals(catalog1.getId(), card.getCatalog().getId());
    }
}