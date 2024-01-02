package com.sparta.jamrello.domain.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.jamrello.domain.board.dto.request.BoardRequestDto;
import com.sparta.jamrello.domain.board.dto.request.InviteMemberDto;
import com.sparta.jamrello.domain.board.dto.response.CatalogListResponseDto;
import com.sparta.jamrello.domain.board.dto.response.BoardResponseDto;
import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.board.repository.BoardRepository;
import com.sparta.jamrello.domain.board.service.BoardServiceImplV1;
import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.catalog.repository.CatalogRepository;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.member.service.MemberService;
import com.sparta.jamrello.domain.memberboard.entity.MemberBoard;
import com.sparta.jamrello.domain.memberboard.entity.MemberBoardRoleEnum;
import com.sparta.jamrello.domain.memberboard.repository.MemberBoardRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@DisplayName("보드 테스트")
class BoardServiceImplV1Test {


    @Autowired
    private BoardServiceImplV1 boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberBoardRepository memberBoardRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CardRepository cardRepository;


    private Member member;
    private Long memberId;

    @BeforeEach
    void createBasicInfo() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
        memberBoardRepository.deleteAll();

        member = memberRepository.save(Member.builder()
                .username("testUser")
                .password("password")
                .nickname("nickname")
                .email("email@email.com")
                .build()
        );

        memberId = member.getId();
    }

    @Test
    @Order(1)
    @DisplayName("보드 생성")
    void successToCreateBoard() {
        // Given
        BoardRequestDto requestDto = new BoardRequestDto("제목", "#000000");
        Board testBoard = Board.fromRequestDto(requestDto);
        MemberBoard testMemberBoard = MemberBoard.createMemberBoard(member, testBoard,
                MemberBoardRoleEnum.ADMIN);

        // When
        BoardResponseDto responseDto = boardService.createBoard(requestDto, member);
        boardRepository.save(testBoard);
        memberBoardRepository.save(testMemberBoard);

        // Then
        assertEquals(requestDto.title(), responseDto.title());
        assertEquals(requestDto.backgroundColor(), responseDto.backgroundColor());
        assertEquals(MemberBoardRoleEnum.ADMIN, testMemberBoard.getRole());
    }

    @Nested
    @DisplayName("보드 수정")
    class BoardUpdateTestList {

        private Long boardId;

        @BeforeEach
        void setUp() {
            BoardRequestDto requestDto = new BoardRequestDto("제목", "#000000");
            Board testBoard = Board.fromRequestDto(requestDto);
            boardRepository.save(testBoard);

            MemberBoard testMemberBoard = MemberBoard.createMemberBoard(member, testBoard,
                    MemberBoardRoleEnum.ADMIN);
            memberBoardRepository.save(testMemberBoard);

            boardId = testBoard.getId();
        }

        @Test
        @Order(2)
        @DisplayName("보드 수정 성공")
        void updateBoard() {
            // Given
            String updateTitle = "제목수정";
            String updateBackgroundColor = "#121212";

            BoardRequestDto requestDto = new BoardRequestDto(updateTitle, updateBackgroundColor);

            Board testUpdateBoard = Board.fromRequestDto(requestDto);

            // When
            BoardResponseDto responseDto = boardService.updateBoard(boardId, requestDto, memberId);

            // Then
            assertEquals(updateTitle, responseDto.title());
            assertEquals(updateBackgroundColor, responseDto.backgroundColor());
        }
    }

    @Nested
    @DisplayName("보드 조회")
    class GetBoardTestList {

        private Board board;
        private Catalog catalog;
        private Card card;

        @BeforeEach
        void setUp() {
            BoardRequestDto requestDto = new BoardRequestDto("제목", "#000000");
            board = boardRepository.save(Board.fromRequestDto(requestDto));

            MemberBoard testMemberBoard = MemberBoard.createMemberBoard(member, board,
                    MemberBoardRoleEnum.ADMIN);
            memberBoardRepository.save(testMemberBoard);

            catalog = catalogRepository.save(Catalog.builder()
                    .board(board)
                    .title("카테고리제목")
                    .status(false)
                    .position(1L)
                    .build());

            card = cardRepository.save(Card.builder()
                    .member(member)
                    .catalog(catalog)
                    .backgroundColor("#ffffff")
                    .title("카드제목")
                    .build());
            catalog.getCardList().add(card);
            board.getCatalogList().add(catalog);
        }

        @Test
        @DisplayName("보드 조회 메서드 getBoard")
        void getBoard() {
            // When
            List<CatalogListResponseDto> list = boardService.getBoard(board.getId());

            // Then
            assertEquals("카테고리제목", list.get(0).title());
            assertEquals("카드제목", list.get(0).fromCardListDtoList().get(0).title());
        }
    }


    @Nested
    @DisplayName("보드 삭제")
    class BoardDeleteTestList {

        private Long boardId;
        private Long memberId;

        @BeforeEach
        void setUp() {
            BoardRequestDto requestDto = new BoardRequestDto("제목", "#000000");
            Board testBoard = Board.fromRequestDto(requestDto);
            boardRepository.save(testBoard);

            MemberBoard testMemberBoard = MemberBoard.createMemberBoard(member, testBoard,
                    MemberBoardRoleEnum.ADMIN);
            memberBoardRepository.save(testMemberBoard);

            boardId = testBoard.getId();
            memberId = testMemberBoard.getMember().getId();
        }

        @Test
        void deleteBoard() {
            // When
            boardService.deleteBoard(boardId, memberId);

            // Then
            Optional<Board> checkDeleteBoard = boardRepository.findById(boardId);
            assertTrue(checkDeleteBoard.isEmpty());

        }
    }

    @Nested
    @DisplayName("이메일 초대 테스트")
    class InviteTest {

        private Long boardId;
        private Long memberId;

        @BeforeEach
        void setUp() {
            BoardRequestDto requestDto = new BoardRequestDto("제목", "#000000");
            Board testBoard = Board.fromRequestDto(requestDto);
            boardRepository.save(testBoard);

            MemberBoard testMemberBoard = MemberBoard.createMemberBoard(member, testBoard,
                    MemberBoardRoleEnum.ADMIN);
            memberBoardRepository.save(testMemberBoard);

            boardId = testBoard.getId();
            memberId = 3L;
        }

        @Test
        void inviteMember() {
            // Given
            InviteMemberDto inviteMemberDto = new InviteMemberDto("email@email.com");

            // When
            boardService.inviteMember(boardId, inviteMemberDto);

            // Then
            assertEquals("email@email.com", inviteMemberDto.email());

        }
    }

}