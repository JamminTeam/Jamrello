package com.sparta.jamrello.domain.comment.service.CommentService;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.comment.dto.CommentRequestDto;
import com.sparta.jamrello.domain.comment.repository.CommentRepository;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.member.repository.entity.MemberRepository;
import com.sparta.jamrello.global.exception.BisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CommentService commentService;

    private CommentRequestDto commentRequestDto;
    private Member member;
    private Card card;

    @BeforeEach
    void setUp() {
        commentRequestDto = new CommentRequestDto("test comment");
        member  = new Member("testUser", "password", "nickname", "email@email.com");
        card = Card.builder()
            .title("Test Card")
            .description("Test Description")
            // 필요한 경우 다른 필드 설정
            .build();
    }

    @Test
    void createCommentTest_Success() {
        // Given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(cardRepository.findById(anyLong())).willReturn(Optional.of(card));
        given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        Comment result = commentService.createComment(1L, 1L, commentRequestDto);

        // Then
        assertNotNull(result);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createCommentTest_MemberNotFound() {
        // Given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThrows(BisException.class, () -> {
            commentService.createComment(1L, 1L, commentRequestDto);
        });
    }

    @Test
    void createCommentTest_CardNotFound() {
        // Given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(cardRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThrows(BisException.class, () -> {
            commentService.createComment(1L, 1L, commentRequestDto);
        });
    }
}
