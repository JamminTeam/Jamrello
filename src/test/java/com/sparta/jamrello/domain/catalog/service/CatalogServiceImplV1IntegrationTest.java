package com.sparta.jamrello.domain.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.board.repository.BoardRepository;
import com.sparta.jamrello.domain.catalog.dto.CatalogPositionRequestDto;
import com.sparta.jamrello.domain.catalog.dto.CatalogRequestDto;
import com.sparta.jamrello.domain.catalog.dto.CatalogResponseDto;
import com.sparta.jamrello.domain.catalog.repository.CatalogRepository;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.memberBoard.entity.MemberBoard;
import com.sparta.jamrello.domain.memberBoard.entity.MemberBoardRoleEnum;
import com.sparta.jamrello.domain.memberBoard.repository.MemberBoardRepository;
import com.sparta.jamrello.global.exception.BisException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class CatalogServiceImplV1IntegrationTest {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private MemberBoardRepository memberBoardRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    private Member member;
    private Board board;
    private MemberBoard memberBoard;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        catalogRepository.deleteAll();
        boardRepository.deleteAll();
        memberBoardRepository.deleteAll();
        catalogService = new CatalogServiceImplV1(memberBoardRepository, boardRepository,
                catalogRepository);
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
    class CatalogSaveTestList {

        @Test
        @DisplayName("카탈로그 저장 테스트 성공 - 멤버")
        void createCatalogSuccessMember() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);

            Long memberId = member.getId();
            Long boardId = board.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);

            // When
            CatalogResponseDto responseDto = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            // Then
            assertNotNull(responseDto);
            assertNotNull(responseDto.title());
            assertNotNull(responseDto.createdAt());
            assertEquals(requestDto.title(), responseDto.title());
            assertEquals(1L, responseDto.position());
        }

        @Test
        @DisplayName("카탈로그 저장 테스트 성공 - 어드민")
        void createCatalogSuccessAdmin() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);

            Long memberId = member.getId();
            Long boardId = board.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);

            // When
            CatalogResponseDto responseDto = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            // Then
            assertNotNull(responseDto);
            assertNotNull(responseDto.title());
            assertNotNull(responseDto.createdAt());
            assertEquals(requestDto.title(), responseDto.title());
            assertEquals(1L, responseDto.position());
        }

        @Test
        @DisplayName("카탈로그 저장 테스트 실패 - 초대는 했으나, 보드 가입 X")
        void createCatalogButInvitedMemberDidNotSignUpInBoard() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.NOT_INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);

            Long memberId = member.getId();
            Long boardId = board.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.createCatalog(boardId, memberId, requestDto));
        }

        @Test
        @DisplayName("카탈로그 저장 테스트 실패 - 보드 가입 X")
        void createCatalogButMemberDidNotSignUpInBoard() {
            // Given
            Long memberId = member.getId();
            Long boardId = board.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);

            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.createCatalog(boardId, memberId, requestDto));
        }
    }

    @Nested
    @DisplayName("카탈로그 제목 업데이트 모음")
    class CatalogUpdateTitleTestList {

        @Test
        @DisplayName("카탈로그 제목 업데이트 성공")
        void updateCatalogTitleSuccess() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);

            Long memberId = member.getId();
            Long boardId = board.getId();
            String title = "제목";
            String titleForUpdate = "수정 제목";
            CatalogRequestDto requestDtoForCreate = new CatalogRequestDto(title);
            CatalogResponseDto createdResponseDto = catalogService.createCatalog(boardId, memberId,
                    requestDtoForCreate);
            Catalog catalog = catalogRepository.findById(createdResponseDto.id()).get();
            Long catalogId = catalog.getId();
            CatalogRequestDto requestDtoForUpdate = new CatalogRequestDto(titleForUpdate);
            // When
            CatalogResponseDto actual = catalogService.updateCatalogTitle(memberId,
                    catalogId, requestDtoForUpdate);
            // Then
            assertNotNull(actual);
            assertEquals(titleForUpdate, actual.title());
            assertEquals(catalogId, actual.id());
            assertEquals(createdResponseDto.position(), actual.position());
            assertEquals(createdResponseDto.createdAt(), actual.createdAt());
        }

        @Test
        @DisplayName("카탈로그 제목 업데이트 성공 - 보드에 가입 된 다른 유저")
        void updateCatalogSuccessTitleOtherMember() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);

            Long memberId = member.getId();
            Long boardId = board.getId();
            String title = "제목";
            String titleForUpdate = "수정 제목";
            CatalogRequestDto requestDtoForCreate = new CatalogRequestDto(title);
            CatalogResponseDto createdResponseDto = catalogService.createCatalog(boardId, memberId,
                    requestDtoForCreate);
            Catalog catalog = catalogRepository.findById(createdResponseDto.id()).get();
            Long catalogId = catalog.getId();
            CatalogRequestDto requestDtoForUpdate = new CatalogRequestDto(titleForUpdate);

            Member memberSignUp = Member.builder()
                    .username("username2")
                    .email("email2@email.com")
                    .password("password")
                    .nickname("nickname2")
                    .build();
            memberSignUp = memberRepository.save(memberSignUp);
            Long memberSignUpId = memberSignUp.getId();
            MemberBoard memberBoardSignUp = new MemberBoard(memberSignUp, board,
                    MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoardSignUp);

            // When
            CatalogResponseDto actual = catalogService.updateCatalogTitle(memberSignUpId,
                    catalogId, requestDtoForUpdate);
            // Then
            assertNotNull(actual);
            assertEquals(titleForUpdate, actual.title());
            assertEquals(catalogId, actual.id());
            assertEquals(createdResponseDto.position(), actual.position());
            assertEquals(createdResponseDto.createdAt(), actual.createdAt());
        }

        @Test
        @DisplayName("카탈로그 제목 업데이트 실패 - 초대는 했으나, 보드에 가입하지 않음")
        void updateCatalogTitleButInvitedMemberDidNotSignUpInBoard() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);

            Long memberId = member.getId();
            Long boardId = board.getId();
            String title = "제목";
            String titleForUpdate = "수정 제목";
            CatalogRequestDto requestDtoForCreate = new CatalogRequestDto(title);
            CatalogResponseDto createdResponseDto = catalogService.createCatalog(boardId, memberId,
                    requestDtoForCreate);
            Catalog catalog = catalogRepository.findById(createdResponseDto.id()).get();
            Long catalogId = catalog.getId();
            CatalogRequestDto requestDtoForUpdate = new CatalogRequestDto(titleForUpdate);
            Member memberNotSignUp = Member.builder()
                    .username("username2")
                    .email("email2@email.com")
                    .password("password")
                    .nickname("nickname2")
                    .build();
            memberNotSignUp = memberRepository.save(memberNotSignUp);
            Long memberNotSignUpId = memberNotSignUp.getId();
            MemberBoard memberBoardNotSignUp = new MemberBoard(memberNotSignUp, board,
                    MemberBoardRoleEnum.NOT_INVITED_MEMBER);
            memberBoardRepository.save(memberBoardNotSignUp);
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.updateCatalogTitle(memberNotSignUpId,
                            catalogId, requestDtoForUpdate));
        }

        @Test
        @DisplayName("카탈로그 제목 업데이트 실패 - 보드에 가입하지 않음")
        void updateCatalogTitleButMemberDidNotSignUpInBoard() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);

            Long memberId = member.getId();
            Long boardId = board.getId();
            String title = "제목";
            String titleForUpdate = "수정 제목";
            CatalogRequestDto requestDtoForCreate = new CatalogRequestDto(title);
            CatalogResponseDto createdResponseDto = catalogService.createCatalog(boardId, memberId,
                    requestDtoForCreate);
            Catalog catalog = catalogRepository.findById(createdResponseDto.id()).get();
            Long catalogId = catalog.getId();
            CatalogRequestDto requestDtoForUpdate = new CatalogRequestDto(titleForUpdate);
            Member memberNotSignUp = Member.builder()
                    .username("username2")
                    .email("email2@email.com")
                    .password("password")
                    .nickname("nickname2")
                    .build();
            memberNotSignUp = memberRepository.save(memberNotSignUp);
            Long memberNotSignUpId = memberNotSignUp.getId();

            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.updateCatalogTitle(memberNotSignUpId,
                            catalogId, requestDtoForUpdate));
        }
    }

    @Nested
    @DisplayName("카탈로그 상태 변경 테스트 모음")
    class CatalogStatusUpdateTestList {

        @Test
        @DisplayName("카탈로그 상태 변경 테스트 성공")
        void updateCatalogStatusSuccess() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto = catalogService.createCatalog(boardId, memberId,
                    requestDto);

            // When
            catalogService.updateCatalogStatus(memberId, responseDto.id());
            Catalog afterCatalog = catalogRepository.findById(responseDto.id()).get();

            // Then
            assertTrue(afterCatalog.isStatus());
        }

        @Test
        @DisplayName("카탈로그 상태 변경 테스트 성공 - 두번 변경")
        void updateCatalogStatusSuccessRe() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto = catalogService.createCatalog(boardId, memberId,
                    requestDto);

            // When
            catalogService.updateCatalogStatus(memberId, responseDto.id());
            catalogService.updateCatalogStatus(memberId, responseDto.id());
            Catalog afterCatalog = catalogRepository.findById(responseDto.id()).get();

            // Then
            assertFalse(afterCatalog.isStatus());
        }

        @Test
        @DisplayName("카탈로그 상태 변경 테스트 실패 - 초대는 했으나, 보드에 가입하지 않음")
        void updateCatalogButInvitedMemberDidNotSignUpInBoard() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto = catalogService.createCatalog(boardId, memberId,
                    requestDto);

            Member notInvitedMember = Member.builder()
                    .username("othername")
                    .email("other@email.com")
                    .password("password")
                    .nickname("othernick")
                    .build();
            memberRepository.save(notInvitedMember);
            MemberBoard notInvitedMemberBoard = new MemberBoard(notInvitedMember, board,
                    MemberBoardRoleEnum.NOT_INVITED_MEMBER);
            memberBoardRepository.save(notInvitedMemberBoard);
            Long notInvitedMemberId = notInvitedMember.getId();
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.updateCatalogStatus(notInvitedMemberId,
                            responseDto.id()));
        }

        @Test
        @DisplayName("카탈로그 상태 변경 테스트 실패 - 보드에 가입하지 않음")
        void updateCatalogButMemberDidNotSignUpInBoard() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto = catalogService.createCatalog(boardId, memberId,
                    requestDto);

            Member notInvitedMember = Member.builder()
                    .username("othername")
                    .email("other@email.com")
                    .password("password")
                    .nickname("othernick")
                    .build();
            memberRepository.save(notInvitedMember);
            Long notInvitedMemberId = notInvitedMember.getId();
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.updateCatalogStatus(notInvitedMemberId,
                            responseDto.id()));
        }
    }

    @Nested
    @DisplayName("카탈로그 삭제 테스트 모음")
    class DeleteCatalogTestList {

        @Test
        @DisplayName("카탈로그 삭제 테스트 성공")
        void deleteCatalogSuccess() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            // When
            catalogService.deleteCatalog(memberId, responseDto.id());
            Optional<Catalog> actual = catalogRepository.findById(responseDto.id());
            // Then
            assertTrue(actual.isEmpty());
        }

        @Test
        @DisplayName("카탈로그 삭제 테스트 실패 - 보드에 초대 했으나 가입하지 않음")
        void deleteCatalogButInvitedMemberDidNotSignUpInBoard() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            Member notInvitedMember = Member.builder()
                    .username("othername")
                    .password("password")
                    .nickname("othernick")
                    .email("other@email.com")
                    .build();
            Member savedMember = memberRepository.save(notInvitedMember);
            MemberBoard notInvitedMemberBoard = new MemberBoard(savedMember, board,
                    MemberBoardRoleEnum.NOT_INVITED_MEMBER);
            memberBoardRepository.save(notInvitedMemberBoard);
            // When - Then

            assertThrows(BisException.class,
                    () -> catalogService.deleteCatalog(savedMember.getId(),
                            responseDto.id()));
        }

        @Test
        @DisplayName("카탈로그 삭제 테스트 실패 - 보드에 가입하지 않음")
        void deleteCatalogButMemberDidNotSignUpInBoard() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            Member notInvitedMember = Member.builder()
                    .username("othername")
                    .password("password")
                    .nickname("othernick")
                    .email("other@email.com")
                    .build();
            Member savedMember = memberRepository.save(notInvitedMember);
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.deleteCatalog(savedMember.getId(),
                            responseDto.id()));
        }
    }

    @Nested
    @DisplayName("카탈로그 순서 이동 테스트 모음")
    class UpdateCatalogPositionTestList {

        @Test
        @DisplayName("카탈로그 순서 변경 테스트 성공 1 2 3 -> 2 1 3")
        void updateCatalogPositionSuccessCaseOne() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto1 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto2 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto3 = catalogService.createCatalog(boardId, memberId,
                    requestDto);

            CatalogPositionRequestDto positionRequestDto = new CatalogPositionRequestDto(2L);
            // When
            catalogService.updateCatalogPos(memberId, responseDto1.id(),
                    positionRequestDto);
            // EntityManager를 clear 하기 때문에 한 번 더 컨택스트 환경에 등록
            Catalog catalog1 = catalogRepository.findById(responseDto1.id()).get();
            Catalog catalog2 = catalogRepository.findById(responseDto2.id()).get();
            Catalog catalog3 = catalogRepository.findById(responseDto3.id()).get();
            // Then
            assertEquals(catalog1.getPosition(), 2L);
            assertEquals(catalog2.getPosition(), 1L);
            assertEquals(catalog3.getPosition(), 3L);
        }

        @Test
        @DisplayName("카탈로그 순서 변경 테스트 성공 1 2 3 -> 1 3 2")
        void updateCatalogPositionSuccessCaseTwo() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto1 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto2 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto3 = catalogService.createCatalog(boardId, memberId,
                    requestDto);

            CatalogPositionRequestDto positionRequestDto = new CatalogPositionRequestDto(2L);
            // When
            catalogService.updateCatalogPos(memberId, responseDto3.id(),
                    positionRequestDto);
            // EntityManager를 clear 하기 때문에 한 번 더 컨택스트 환경에 등록
            Catalog catalog1 = catalogRepository.findById(responseDto1.id()).get();
            Catalog catalog2 = catalogRepository.findById(responseDto2.id()).get();
            Catalog catalog3 = catalogRepository.findById(responseDto3.id()).get();
            // Then
            assertEquals(catalog1.getPosition(), 1L);
            assertEquals(catalog2.getPosition(), 3L);
            assertEquals(catalog3.getPosition(), 2L);
        }

        @Test
        @DisplayName("카탈로그 순서 변경 테스트 실패 - 보관된 카탈로그 개수보다 큰 값")
        void updateCatalogPositionButItsOver() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto1 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto2 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto3 = catalogService.createCatalog(boardId, memberId,
                    requestDto);

            CatalogPositionRequestDto positionRequestDto = new CatalogPositionRequestDto(4L);
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.updateCatalogPos(memberId, responseDto1.id(),
                            positionRequestDto));
        }

        @Test
        @DisplayName("카탈로그 순서 변경 테스트 실패 - 1보다 작은 값")
        void updateCatalogPositionButItsOverZero() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto1 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto2 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto3 = catalogService.createCatalog(boardId, memberId,
                    requestDto);

            CatalogPositionRequestDto positionRequestDto = new CatalogPositionRequestDto(0L);
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.updateCatalogPos(memberId, responseDto1.id(),
                            positionRequestDto));
        }

        @Test
        @DisplayName("카탈로그 순서 변경 테스트 실패 - 0보다 작은 값")
        void updateCatalogPositionButItsOverMinus() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto1 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto2 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto3 = catalogService.createCatalog(boardId, memberId,
                    requestDto);

            CatalogPositionRequestDto positionRequestDto = new CatalogPositionRequestDto(-1L);
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.updateCatalogPos(memberId, responseDto1.id(),
                            positionRequestDto));
        }

        @Test
        @DisplayName("카탈로그 순서 변경 테스트 실패 - 보드에 초대는 했지만 가입하지 않은 유저")
        void updateCatalogPositionButInvitedMemberDidNotSignUpInBoard() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto1 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto2 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto3 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            Member notInvitedMember = Member.builder()
                    .username("othername")
                    .password("password")
                    .nickname("othernick")
                    .email("other@email.com")
                    .build();
            Member savedMember = memberRepository.save(notInvitedMember);
            MemberBoard notInvitedMemberBoard = new MemberBoard(savedMember, board,
                    MemberBoardRoleEnum.NOT_INVITED_MEMBER);
            memberBoardRepository.save(notInvitedMemberBoard);

            CatalogPositionRequestDto positionRequestDto = new CatalogPositionRequestDto(-1L);
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.updateCatalogPos(savedMember.getId(),
                            responseDto1.id(),
                            positionRequestDto));
        }

        @Test
        @DisplayName("카탈로그 순서 변경 테스트 실패 - 보드에 가입하지 않은 유저")
        void updateCatalogPositionButMemberDidNotSignUpInBoard() {
            // Given
            memberBoard = new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER);
            memberBoardRepository.save(memberBoard);
            Long boardId = board.getId();
            Long memberId = member.getId();
            String title = "제목";
            CatalogRequestDto requestDto = new CatalogRequestDto(title);
            CatalogResponseDto responseDto1 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto2 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            CatalogResponseDto responseDto3 = catalogService.createCatalog(boardId, memberId,
                    requestDto);
            Member notInvitedMember = Member.builder()
                    .username("othername")
                    .password("password")
                    .nickname("othernick")
                    .email("other@email.com")
                    .build();
            Member savedMember = memberRepository.save(notInvitedMember);

            CatalogPositionRequestDto positionRequestDto = new CatalogPositionRequestDto(-1L);
            // When - Then
            assertThrows(BisException.class,
                    () -> catalogService.updateCatalogPos(savedMember.getId(),
                            responseDto1.id(),
                            positionRequestDto));
        }
    }
}