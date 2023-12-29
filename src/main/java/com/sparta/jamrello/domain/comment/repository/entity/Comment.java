package com.sparta.jamrello.domain.comment.repository.entity;

import com.sparta.jamrello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.jamrello.domain.comment.dto.response.CommentResponseDto;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.global.time.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "comments")
public class Comment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    private String content;

    @Column(name = "comment_image")
    private String commentImageUrl;

    @Builder
    public Comment(Member member, Card card, String content) {
        this.member = member;
        this.card = card;
        this.content = content;
    }

    public static Comment createCommentOf(String content, Member member, Card card) {
        return Comment.builder()
            .content(content)
            .member(member)
            .card(card)
            .build();
    }

    public static CommentResponseDto toCommentResponse(Comment comment) {
        return new CommentResponseDto(comment.getMember().getNickname(), comment.getContent(),
            comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public void updateComment(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.content();
    }

    public void updateImageUrl(String commentImageUrl) {
        this.commentImageUrl = commentImageUrl;
    }

    public void removeImageUrl(String fileName) {
        this.commentImageUrl = null;
    }


}
