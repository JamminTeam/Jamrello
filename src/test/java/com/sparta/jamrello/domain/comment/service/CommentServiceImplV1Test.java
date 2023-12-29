package com.sparta.jamrello.domain.comment.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.comment.dto.CommentRequestDto;
import com.sparta.jamrello.domain.comment.dto.CommentResponseDto;
import com.sparta.jamrello.domain.comment.repository.CommentRepository;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.exception.BisException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "test")
@SpringBootTest
@DisplayName("댓글 서비스 통합 테스트")
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CommentService commentService;

    private CommentRequestDto commentRequestDto;
    private Member member;
    private Card card;
    private Comment comment;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.builder()
            .username("testUser")
            .password("password")
            .nickname("nickname")
            .email("email@email.com")
            .build()
        );

        card = cardRepository.save(Card.builder()
            .title("Test Card")
            .description("Test Description")
            .build());
        commentRequestDto = new CommentRequestDto("test comment");
        comment = commentRepository.save(
            Comment.createCommentOf(commentRequestDto.content(), member, card));

    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        cardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void createCommentTest() {
        // Given
        member = memberRepository.save(Member.builder()
            .username("newtestUser")
            .password("newpassword")
            .nickname("newnickname")
            .email("newemail@email.com")
            .build()
        );
        Long memberId = member.getId();
        Long cardId = card.getId();

        // When
        Comment createdComment = commentRepository.save(
            commentService.createComment(memberId, cardId, commentRequestDto));

        // Then
        assertNotNull(createdComment);
        assertEquals(commentRequestDto.content(), createdComment.getContent());
        assertEquals(memberId, createdComment.getMember().getId());
        assertEquals(cardId, createdComment.getCard().getId());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void test3() {
        // Given
        Comment commentToDelete = comment;

        // When
        commentService.deleteComment(commentToDelete.getId(), member.getId());

        // Then
        assertThrows(BisException.class, () -> commentService.getComment(commentToDelete.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 회원 예외")
    void findMember_NOT_FOUND_MEMBER() {
        // Given
        Long invalidMemberId = 999L;

        // When & Then
        assertThrows(BisException.class,
            () -> commentService.createComment(invalidMemberId, card.getId(), commentRequestDto));

    }

    @Test
    @DisplayName("존재하지 않는 카드 예외")
    void findCard_NOT_FOUND_CARD() {
        // Given
        Long invalidCardId = 999L;

        // When & Then
        assertThrows(BisException.class,
            () -> commentService.createComment(member.getId(), invalidCardId, commentRequestDto));
    }
    @Test
    @DisplayName("존재하지 않는 댓글 예외")
    void findComment_NOT_FOUND_COMMENT() {
        // Given
        Long invalidCardId = 999L;

        // When & Then
        assertThrows(BisException.class,
            () -> commentService.createComment(member.getId(), invalidCardId, commentRequestDto));
    }

    @Test
    @DisplayName("commentId로 멤버 찾기 테스트")
    void findMemberByCommentIdTest() {
        // Given
        Long commentId = comment.getId();

        Member foundMember = commentService.getMemberByCommentId(commentId);

        // Then
        assertNotNull(foundMember);
        assertEquals(member.getId(), foundMember.getId());
    }

    @Nested
    @DisplayName("댓글 조회 테스트")
    class GetComment {

        @Test
        @DisplayName("댓글 단건 조회 테스트")
        void getCommentTest() {
            // Given
            Comment expectedComment = comment;

            // When
            Comment foundComment = commentService.getComment(expectedComment.getId());

            // Then
            assertNotNull(foundComment);
            assertEquals(expectedComment.getId(), foundComment.getId());
        }

        @Test
        @DisplayName("댓글 목록 페이징 적용 Test")
        void getCommentListWithPagenation() {
            // Given
            for (int i = 0; i < 20; i++) {
                comment = commentRepository.save(
                    Comment.createCommentOf(commentRequestDto.content(), member, card));
            }
            int pageSize = 10;
            Pageable pageable = PageRequest.of(0, pageSize);

            // When
            List<CommentResponseDto> commentResponseDtoList = commentService.getComments(pageable);

            // Then
            assertNotNull(commentResponseDtoList);
            assertEquals(pageSize, commentResponseDtoList.size());
        }
    }

    @Nested
    @DisplayName("댓글 수정 테스트")
    class WhenUpdateComment {

        @Test
        @DisplayName("댓글 수정 테스트")
        void updateComment_Success() {
            // Given
            CommentRequestDto updatedCommentRequestDto = new CommentRequestDto("updated comment");

            // When
            Comment updatedComment = commentService.updateComment(comment.getId(), member.getId(),
                updatedCommentRequestDto);

            // Then
            assertNotNull(updatedComment);
            assertEquals(updatedCommentRequestDto.content(), updatedComment.getContent());
        }

        @Test
        @DisplayName("허가되지 않는 유저의 수정 시도")
        void updatedComment_UnAuhorizedMember() {
            // Given
            Comment originalComment = comment;
            Member unauthorizedMember = memberRepository.save(
                Member.createMember("badUser", "password", "badnickname",
                    "bad@bad.com")); // 다른 사용자 정보

            // When & Then
            assertThrows(BisException.class,
                () -> commentService.updateComment(originalComment.getId(),
                    unauthorizedMember.getId(),
                    commentRequestDto));
        }

    }


}
