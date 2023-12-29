package com.sparta.jamrello.domain.comment.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = "test")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CardRepository cardRepository;

    private Member member;
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
        Card card = cardRepository.save(Card.builder()
            .title("Test Card")
            .build());
        comment = commentRepository.save(Comment.builder()
            .member(member)
            .card(card)
            .content("test comment")
            .build());
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        cardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("특정 Comment ID를 가진 Member 찾기")
    void findMemberByCommentIdTest() {
        Optional<Member> foundMember = commentRepository.findMemberByCommentId(comment.getId());
        Long test = comment.getId();

        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("모든 댓글 페이징하여 조회")
    void findAllCommentsWithPaginationTest() {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<Comment> commentsPage = commentRepository.findAllCommentsWithPagination(
            pageable);

        assertThat(commentsPage).isNotNull();
        assertThat(commentsPage.getContent()).isNotNull();
        Assertions.assertEquals(commentsPage.getSize(), pageSize);
    }

}