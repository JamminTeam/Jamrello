package com.sparta.jamrello.domain.comment.repository.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void createComment_Success() {

        Member member = new Member("testUser", "password", "nickname", "email@email.com");
        Card card = Card.builder()
                .title("Test Card")
                .backgroundColor("#ffffff")
                .build();

        Comment comment = Comment.builder()
                .member(member)
                .card(card)
                .content("Test comment content")
                .build();

        // 검증
        assertNotNull(comment);
        assertEquals(member, comment.getMember());
        assertEquals(card, comment.getCard());
        assertEquals("Test comment content", comment.getContent());
    }
}
