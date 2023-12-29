//package com.sparta.jamrello.domain.catalog.repository.entity;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.sparta.jamrello.domain.board.dto.request.BoardRequestDto;
//import com.sparta.jamrello.domain.board.entity.Board;
//import com.sparta.jamrello.domain.catalog.dto.CatalogRequestDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.ActiveProfiles;
//
//@DataJpaTest
//@ActiveProfiles(profiles = "test")
//class CatalogTest {
//
//    @Autowired
//    private TestEntityManager em;
//
//    private Board board;
//
//    @BeforeEach
//    void setUp() {
//        BoardRequestDto boardRequestDto = new BoardRequestDto("title", "#ffffff");
//        board = Board.fromRequestDto(boardRequestDto);
//        em.persist(board);
//    }
//
//    @Test
//    @DisplayName("카탈로그 저장 테스트")
//    void 카탈로그_저장_테스트() {
//        // Given
//        String title = "제목";
//        CatalogRequestDto requestDto = new CatalogRequestDto(title);
//        Long position = 2L;
//        Catalog catalog = Catalog.createCatalog(board, requestDto, position);
//        // When
//        Catalog result = em.persistAndFlush(catalog);
//        // Then
//        assertThat(result).isEqualTo(catalog);
//        assertThat(result.getBoard()).isEqualTo(board);
//        assertThat(result.getTitle()).isEqualTo(catalog.getTitle());
//        assertThat(result.getPosition()).isEqualTo(catalog.getPosition());
//        assertThat(result.isStatus()).isFalse();
//    }
//
//}