package com.sparta.jamrello.domain.catalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.board.repository.BoardRepository;
import com.sparta.jamrello.domain.catalog.dto.CatalogRequestDto;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class CatalogRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    private Board board;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.builder()
                .username("username")
                .password("password")
                .nickname("nickname")
                .email("email@email.com")
                .build());


        board = boardRepository.save(Board.builder()
                .title("title")
                .backgroundColor("#ffffff")
                .build());
    }

    @Nested
    @DisplayName("카탈로그 저장 테스트 모음")
    class CatalogSaveTests {

        @Test
        @DisplayName("카탈로그 저장 테스트 정상적인 저장")
        void saveCatalog() {
            // Given
            String title = "title";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            Long position = 2L;

            Catalog catalog = Catalog.createCatalog(board, requestDto, position);
            // When
            Catalog result = catalogRepository.save(catalog);
            // Then
            assertNotNull(result);
            assertEquals(catalog.getTitle(), result.getTitle());
            assertEquals(catalog.getPosition(), result.getPosition());
        }

        @Test
        @DisplayName("카탈로그 저장 테스트 만약 타이틀이 빈 값이면 예외를 발생시킨다")
        void catalogSaveButTitleIsNull() {
            // Given
            String title = null;
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            Long position = 2L;

            Catalog catalog = Catalog.createCatalog(board, requestDto, position);
            // When - Then
            assertThrows(Exception.class, () -> catalogRepository.save(catalog));
        }

        @Test
        @DisplayName("카탈로그 저장 테스트 만약 포지션이 빈 값이면 예외를 발생시킨다")
        void catalogSaveButPositionIsNull() {
            // Given
            String title = "title";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            Long position = null;

            Catalog catalog = Catalog.createCatalog(board, requestDto, position);
            // When - Then
            assertThrows(Exception.class, () -> catalogRepository.save(catalog));
        }
    }

    @Nested
    @DisplayName("카탈로그 업데이트 테스트 모음")
    class CatalogUpdateTests {

        @Test
        @DisplayName("카탈로그 업데이트 테스트 정상적인 업데이트")
        void catalogUpdate() {
            // Given
            String title = "title";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            Long position = 2L;
            Catalog catalog = Catalog.createCatalog(board, requestDto, position);
            Catalog savedCatalog = catalogRepository.save(catalog);

            String titleUpdate = "update";
            CatalogRequestDto requestDtoForUpdate = new CatalogRequestDto(titleUpdate);
            savedCatalog.updateCatalogTitle(requestDtoForUpdate);
            // When
            Catalog result = catalogRepository.save(savedCatalog);
            // Then
            assertNotNull(result);
            assertNotEquals(title, result.getTitle());
            assertEquals(titleUpdate, result.getTitle());
            assertEquals(catalog.getPosition(), result.getPosition());
        }

        @Test
        @DisplayName("카탈로그 업데이트 테스트 하지만 타이틀이 NULL")
        void catalogUpdateButTitleIsNull() {
            // Given
            String title = "title";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            Long position = 2L;
            Catalog catalog = Catalog.createCatalog(board, requestDto, position);
            Catalog savedCatalog = catalogRepository.save(catalog);

            String titleUpdate = null;
            CatalogRequestDto requestDtoForUpdate = new CatalogRequestDto(titleUpdate);
            savedCatalog.updateCatalogTitle(requestDtoForUpdate);

            // When - Then
            assertThrows(Exception.class, () -> em.flush());
        }
    }
}