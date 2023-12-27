package com.sparta.jamrello.domain.comment.repository.entity;

import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.global.time.TimeStamp;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Builder
    public Comment(Member member, Card card, String content) {
        this.member = member;
        this.card = card;
        this.content = content;
    }
}
